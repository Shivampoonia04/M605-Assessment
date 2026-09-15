package com.shivampoonia.reeldock.service;

import com.shivampoonia.reeldock.dto.ReviewRequest;
import com.shivampoonia.reeldock.dto.Views.ReviewView;
import com.shivampoonia.reeldock.exception.NotFoundException;
import com.shivampoonia.reeldock.exception.RuleBreachException;
import com.shivampoonia.reeldock.model.Member;
import com.shivampoonia.reeldock.model.Production;
import com.shivampoonia.reeldock.model.Review;
import com.shivampoonia.reeldock.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviews;
    private final ProductionService productions;
    private final MemberService members;

    public ReviewService(ReviewRepository reviews, ProductionService productions, MemberService members) {
        this.reviews = reviews;
        this.productions = productions;
        this.members = members;
    }

    @Transactional(readOnly = true)
    public List<ReviewView> list() {
        return reviews.allFetched().stream().map(ReviewView::of).toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewView> forProduction(Long productionId) {
        productions.entity(productionId);
        return reviews.fetchedByProduction(productionId).stream().map(ReviewView::of).toList();
    }

    public ReviewView post(ReviewRequest req) {
        Production production = productions.entity(req.getProductionId());
        Member member = members.entity(req.getMemberId());
        if (production.getOwner().getId().equals(member.getId())) {
            throw new RuleBreachException("Owners cannot mark their own cut");
        }
        if (reviews.existsByProductionIdAndMemberId(production.getId(), member.getId())) {
            throw new RuleBreachException("This member already reviewed that cut");
        }
        Review r = new Review();
        r.setProduction(production);
        r.setMember(member);
        r.setStars(req.getStars());
        r.setBody(req.getBody());
        return ReviewView.of(reviews.save(r));
    }

    public void erase(Long id) {
        Review r = reviews.findById(id).orElseThrow(() -> new NotFoundException("Review " + id + " not found"));
        reviews.delete(r);
    }
}
