package com.shivampoonia.reeldock.service;

import com.shivampoonia.reeldock.dto.Views.CodecCount;
import com.shivampoonia.reeldock.dto.Views.DeskView;
import com.shivampoonia.reeldock.dto.Views.KindRating;
import com.shivampoonia.reeldock.dto.Views.KitLoad;
import com.shivampoonia.reeldock.dto.Views.LoanView;
import com.shivampoonia.reeldock.dto.Views.MemberView;
import com.shivampoonia.reeldock.dto.Views.ProductionView;
import com.shivampoonia.reeldock.dto.Views.ReviewView;
import com.shivampoonia.reeldock.exception.NotFoundException;
import com.shivampoonia.reeldock.model.KitKind;
import com.shivampoonia.reeldock.model.KitStatus;
import com.shivampoonia.reeldock.model.Member;
import com.shivampoonia.reeldock.model.ProductionKind;
import com.shivampoonia.reeldock.repository.KitRepository;
import com.shivampoonia.reeldock.repository.LoanRepository;
import com.shivampoonia.reeldock.repository.MemberRepository;
import com.shivampoonia.reeldock.repository.ProductionRepository;
import com.shivampoonia.reeldock.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class DeskService {

    private final MemberRepository members;
    private final LoanRepository loans;
    private final ProductionRepository productions;
    private final ReviewRepository reviews;
    private final KitRepository kits;

    public DeskService(MemberRepository members, LoanRepository loans, ProductionRepository productions,
                       ReviewRepository reviews, KitRepository kits) {
        this.members = members;
        this.loans = loans;
        this.productions = productions;
        this.reviews = reviews;
        this.kits = kits;
    }

    public DeskView desk(String campusId) {
        Member member = members.findByCampusId(campusId)
                .orElseThrow(() -> new NotFoundException("No member with campus id " + campusId));
        List<LoanView> loanViews = loans.deskForMember(member.getId()).stream().map(LoanView::of).toList();
        List<ProductionView> cuts = productions.byOwner(member.getId()).stream().map(ProductionView::of).toList();
        List<ReviewView> marks = reviews.byReviewer(member.getId()).stream().map(ReviewView::of).toList();
        return new DeskView(MemberView.of(member), loanViews, cuts, marks);
    }

    public List<KindRating> ratings() {
        return reviews.ratingsByKind().stream()
                .map(row -> new KindRating(
                        ((ProductionKind) row[0]).name(),
                        row[1] == null ? 0 : ((Number) row[1]).doubleValue(),
                        ((Number) row[2]).longValue()))
                .toList();
    }

    public List<CodecCount> codecs() {
        return productions.codecCounts().stream()
                .map(row -> new CodecCount(
                        row[0] == null ? "(none)" : String.valueOf(row[0]),
                        ((Number) row[1]).longValue()))
                .toList();
    }

    public List<KitLoad> kitLoad() {
        return kits.loadByKind().stream()
                .map(row -> new KitLoad(
                        ((KitKind) row[0]).name(),
                        ((KitStatus) row[1]).name(),
                        ((Number) row[2]).longValue()))
                .toList();
    }

    public Map<String, Long> counts() {
        Map<String, Long> out = new LinkedHashMap<>();
        out.put("members", members.count());
        out.put("kits", kits.count());
        out.put("loans", loans.count());
        out.put("productions", productions.count());
        out.put("reviews", reviews.count());
        return out;
    }
}
