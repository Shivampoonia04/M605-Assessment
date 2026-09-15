package com.shivampoonia.reeldock.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Table(
        name = "productions",
        indexes = {
                @Index(name = "idx_productions_owner", columnList = "owner_id"),
                @Index(name = "idx_productions_kind_status", columnList = "kind,cut_status")
        }
)
@Check(constraints = "runtime_sec >= 0")
public class Production {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String slug;

    @Column(nullable = false, length = 160)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductionKind kind;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Member owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kit_id")
    private Kit kit;

    @Column(nullable = false)
    private int runtimeSec;

    @Enumerated(EnumType.STRING)
    @Column(name = "cut_status", nullable = false, length = 20)
    private CutStatus cutStatus = CutStatus.DRAFT;

    /**
     * Flexible production card: codec, crew list, chapters.
     * Crew is embedded because it is small and always read with the title.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "json")
    private Map<String, Object> payload = new LinkedHashMap<>();

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public ProductionKind getKind() { return kind; }
    public void setKind(ProductionKind kind) { this.kind = kind; }
    public Member getOwner() { return owner; }
    public void setOwner(Member owner) { this.owner = owner; }
    public Kit getKit() { return kit; }
    public void setKit(Kit kit) { this.kit = kit; }
    public int getRuntimeSec() { return runtimeSec; }
    public void setRuntimeSec(int runtimeSec) { this.runtimeSec = runtimeSec; }
    public CutStatus getCutStatus() { return cutStatus; }
    public void setCutStatus(CutStatus cutStatus) { this.cutStatus = cutStatus; }
    public Map<String, Object> getPayload() { return payload; }
    public void setPayload(Map<String, Object> payload) { this.payload = payload; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
