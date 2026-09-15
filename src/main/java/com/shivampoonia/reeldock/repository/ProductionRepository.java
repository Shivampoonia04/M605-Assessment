package com.shivampoonia.reeldock.repository;

import com.shivampoonia.reeldock.model.Production;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductionRepository extends JpaRepository<Production, Long> {

    Optional<Production> findBySlug(String slug);

    @Query("""
            SELECT p FROM Production p
            JOIN FETCH p.owner
            LEFT JOIN FETCH p.kit
            ORDER BY p.id
            """)
    List<Production> allFetched();

    @Query("""
            SELECT p FROM Production p
            JOIN FETCH p.owner
            LEFT JOIN FETCH p.kit
            WHERE p.id = :id
            """)
    Optional<Production> findFetched(@Param("id") Long id);

    @Query("""
            SELECT p FROM Production p
            JOIN FETCH p.owner
            LEFT JOIN FETCH p.kit
            WHERE p.owner.id = :ownerId
            ORDER BY p.createdAt DESC
            """)
    List<Production> byOwner(@Param("ownerId") Long ownerId);

    @Query(value = """
            SELECT payload->>'$.codec' AS codec, COUNT(*) AS n
            FROM productions
            GROUP BY payload->>'$.codec'
            ORDER BY n DESC
            """, nativeQuery = true)
    List<Object[]> codecCounts();
}
