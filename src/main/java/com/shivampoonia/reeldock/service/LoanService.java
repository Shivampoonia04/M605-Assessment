package com.shivampoonia.reeldock.service;

import com.shivampoonia.reeldock.dto.LoanRequest;
import com.shivampoonia.reeldock.dto.Views.LoanView;
import com.shivampoonia.reeldock.exception.NotFoundException;
import com.shivampoonia.reeldock.exception.RuleBreachException;
import com.shivampoonia.reeldock.model.Kit;
import com.shivampoonia.reeldock.model.KitStatus;
import com.shivampoonia.reeldock.model.Loan;
import com.shivampoonia.reeldock.model.LoanStatus;
import com.shivampoonia.reeldock.model.Member;
import com.shivampoonia.reeldock.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class LoanService {

    private final LoanRepository loans;
    private final MemberService members;
    private final KitService kits;
    private final SecureRandom random = new SecureRandom();

    @Value("${reeldock.max-loan-hours:72}")
    private int maxHours;

    public LoanService(LoanRepository loans, MemberService members, KitService kits) {
        this.loans = loans;
        this.members = members;
        this.kits = kits;
    }

    @Transactional(readOnly = true)
    public List<LoanView> list() {
        return loans.allFetched().stream().map(LoanView::of).toList();
    }

    @Transactional(readOnly = true)
    public LoanView one(Long id) {
        return LoanView.of(loans.findFetched(id)
                .orElseThrow(() -> new NotFoundException("Loan " + id + " not found")));
    }

    @Transactional(readOnly = true)
    public List<LoanView> overdue() {
        return loans.overdue(LoanStatus.OPEN, LocalDateTime.now()).stream().map(LoanView::of).toList();
    }

    public LoanView checkout(LoanRequest req) {
        guardWindow(req.getTakenAt(), req.getDueAt());
        Member member = members.entity(req.getMemberId());
        Kit kit = kits.entity(req.getKitId());

        if (kit.getStatus() != KitStatus.READY) {
            throw new RuleBreachException("Kit " + kit.getTag() + " is not on the ready rack");
        }
        if (loans.countByKitIdAndStatus(kit.getId(), LoanStatus.OPEN) > 0) {
            throw new RuleBreachException("That kit already has an open loan");
        }
        if (loans.countByMemberIdAndStatus(member.getId(), LoanStatus.OPEN) > 0) {
            throw new RuleBreachException("Return the open kit before taking another");
        }

        Loan loan = new Loan();
        loan.setMember(member);
        loan.setKit(kit);
        loan.setTakenAt(req.getTakenAt());
        loan.setDueAt(req.getDueAt());
        loan.setNote(req.getNote());
        loan.setStatus(LoanStatus.OPEN);
        loan.setLockerPin(pin());
        kit.setStatus(KitStatus.OUT);
        return LoanView.of(loans.save(loan));
    }

    public LoanView giveBack(Long id) {
        Loan loan = entity(id);
        if (loan.getStatus() != LoanStatus.OPEN) {
            throw new RuleBreachException("Loan is already closed");
        }
        LocalDateTime now = LocalDateTime.now();
        loan.setReturnedAt(now);
        loan.setStatus(now.isAfter(loan.getDueAt()) ? LoanStatus.LATE : LoanStatus.RETURNED);
        loan.getKit().setStatus(KitStatus.READY);
        return LoanView.of(loans.save(loan));
    }

    public void erase(Long id) {
        Loan loan = entity(id);
        if (loan.getStatus() == LoanStatus.OPEN) {
            loan.getKit().setStatus(KitStatus.READY);
        }
        loans.delete(loan);
    }

    public Loan entity(Long id) {
        return loans.findById(id).orElseThrow(() -> new NotFoundException("Loan " + id + " not found"));
    }

    private void guardWindow(LocalDateTime takenAt, LocalDateTime dueAt) {
        if (!dueAt.isAfter(takenAt)) {
            throw new RuleBreachException("Due time must be after pickup");
        }
        long hours = Duration.between(takenAt, dueAt).toHours();
        if (hours > maxHours) {
            throw new RuleBreachException("Loans cannot exceed " + maxHours + " hours");
        }
    }

    private String pin() {
        return String.format("%06d", random.nextInt(1_000_000));
    }
}
