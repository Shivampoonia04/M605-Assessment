package com.shivampoonia.reeldock.repository;

import com.shivampoonia.reeldock.model.Loan;
import com.shivampoonia.reeldock.model.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("""
            SELECT l FROM Loan l
            JOIN FETCH l.member
            JOIN FETCH l.kit
            ORDER BY l.id
            """)
    List<Loan> allFetched();

    @Query("""
            SELECT l FROM Loan l
            JOIN FETCH l.member
            JOIN FETCH l.kit
            WHERE l.id = :id
            """)
    Optional<Loan> findFetched(@Param("id") Long id);

    List<Loan> findByMemberId(Long memberId);

    List<Loan> findByKitId(Long kitId);

    long countByMemberIdAndStatus(Long memberId, LoanStatus status);

    long countByKitIdAndStatus(Long kitId, LoanStatus status);

    @Query("""
            SELECT l FROM Loan l
            JOIN FETCH l.member
            JOIN FETCH l.kit
            WHERE l.status = :status AND l.dueAt < :now
            ORDER BY l.dueAt
            """)
    List<Loan> overdue(@Param("status") LoanStatus status, @Param("now") LocalDateTime now);

    @Query("""
            SELECT l FROM Loan l
            JOIN FETCH l.member
            JOIN FETCH l.kit
            WHERE l.member.id = :memberId
            ORDER BY l.takenAt DESC
            """)
    List<Loan> deskForMember(@Param("memberId") Long memberId);
}
