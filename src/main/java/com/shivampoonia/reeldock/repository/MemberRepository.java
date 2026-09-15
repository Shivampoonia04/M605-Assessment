package com.shivampoonia.reeldock.repository;

import com.shivampoonia.reeldock.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByCampusId(String campusId);
    boolean existsByCampusId(String campusId);
    boolean existsByMail(String mail);
}
