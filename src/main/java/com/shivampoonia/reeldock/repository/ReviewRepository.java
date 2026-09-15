package com.shivampoonia.reeldock.repository;

import com.shivampoonia.reeldock.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("""
            SELECT r FROM Review r
            JOIN FETCH r.member
            JOIN FETCH r.production
            ORDER BY r.id
            """)
    List<Review> allFetched();

    @Query("""
            SELECT r FROM Review r
            JOIN FETCH r.member
            JOIN FETCH r.production
            WHERE r.production.id = :productionId
            ORDER BY r.postedAt DESC
            """)
    List<Review> fetchedByProduction(@Param("productionId") Long productionId);

    List<Review> findByProductionId(Long productionId);

    boolean existsByProductionIdAndMemberId(Long productionId, Long memberId);

    @Query("""
            SELECT p.kind, AVG(r.stars), COUNT(r)
            FROM Review r
            JOIN r.production p
            GROUP BY p.kind
            ORDER BY AVG(r.stars) DESC
            """)
    List<Object[]> ratingsByKind();

    @Query("""
            SELECT r FROM Review r
            JOIN FETCH r.member
            JOIN FETCH r.production
            WHERE r.member.id = :memberId
            ORDER BY r.postedAt DESC
            """)
    List<Review> byReviewer(@Param("memberId") Long memberId);
}
