package com.shivampoonia.reeldock.service;

import com.shivampoonia.reeldock.dto.MemberRequest;
import com.shivampoonia.reeldock.dto.Views.MemberView;
import com.shivampoonia.reeldock.exception.NotFoundException;
import com.shivampoonia.reeldock.exception.RuleBreachException;
import com.shivampoonia.reeldock.model.Member;
import com.shivampoonia.reeldock.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberService {

    private final MemberRepository members;

    public MemberService(MemberRepository members) {
        this.members = members;
    }

    @Transactional(readOnly = true)
    public List<MemberView> list() {
        return members.findAll().stream().map(MemberView::of).toList();
    }

    @Transactional(readOnly = true)
    public MemberView one(Long id) {
        return MemberView.of(entity(id));
    }

    @Transactional(readOnly = true)
    public MemberView byCampus(String campusId) {
        return MemberView.of(members.findByCampusId(campusId)
                .orElseThrow(() -> new NotFoundException("No member with campus id " + campusId)));
    }

    public MemberView create(MemberRequest req) {
        if (members.existsByCampusId(req.getCampusId())) {
            throw new RuleBreachException("Campus id already on file");
        }
        if (members.existsByMail(req.getMail())) {
            throw new RuleBreachException("Mail already on file");
        }
        Member m = new Member();
        apply(m, req);
        return MemberView.of(members.save(m));
    }

    public MemberView rewrite(Long id, MemberRequest req) {
        Member m = entity(id);
        if (!m.getCampusId().equals(req.getCampusId()) && members.existsByCampusId(req.getCampusId())) {
            throw new RuleBreachException("Campus id already on file");
        }
        if (!m.getMail().equals(req.getMail()) && members.existsByMail(req.getMail())) {
            throw new RuleBreachException("Mail already on file");
        }
        apply(m, req);
        return MemberView.of(members.save(m));
    }

    public void erase(Long id) {
        members.delete(entity(id));
    }

    public Member entity(Long id) {
        return members.findById(id).orElseThrow(() -> new NotFoundException("Member " + id + " not found"));
    }

    private void apply(Member m, MemberRequest req) {
        m.setCampusId(req.getCampusId());
        m.setDisplayName(req.getDisplayName());
        m.setMail(req.getMail());
        m.setProgramme(req.getProgramme());
        m.setCohort(req.getCohort());
    }
}
