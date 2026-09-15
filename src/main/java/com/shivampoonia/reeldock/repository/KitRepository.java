package com.shivampoonia.reeldock.repository;

import com.shivampoonia.reeldock.model.Kit;
import com.shivampoonia.reeldock.model.KitKind;
import com.shivampoonia.reeldock.model.KitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface KitRepository extends JpaRepository<Kit, Long> {
    Optional<Kit> findByTag(String tag);
    List<Kit> findByKindAndStatus(KitKind kind, KitStatus status);

    @Query("""
            SELECT k.kind, k.status, COUNT(k)
            FROM Kit k
            GROUP BY k.kind, k.status
            ORDER BY k.kind, k.status
            """)
    List<Object[]> loadByKind();
}
