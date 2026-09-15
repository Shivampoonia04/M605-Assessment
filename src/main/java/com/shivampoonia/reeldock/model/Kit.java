package com.shivampoonia.reeldock.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.hibernate.annotations.Check;

@Entity
@Table(
        name = "kits",
        indexes = {
                @Index(name = "idx_kits_kind_status", columnList = "kind,status"),
                @Index(name = "idx_kits_bay", columnList = "bay")
        }
)
@Check(constraints = "day_rate_cents >= 0")
public class Kit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String tag;

    @Column(nullable = false, length = 120)
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private KitKind kind;

    @Column(nullable = false, unique = true, length = 60)
    private String serial;

    @Column(nullable = false, length = 40)
    private String bay;

    @Column(nullable = false)
    private int dayRateCents;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private KitStatus status = KitStatus.READY;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public KitKind getKind() { return kind; }
    public void setKind(KitKind kind) { this.kind = kind; }
    public String getSerial() { return serial; }
    public void setSerial(String serial) { this.serial = serial; }
    public String getBay() { return bay; }
    public void setBay(String bay) { this.bay = bay; }
    public int getDayRateCents() { return dayRateCents; }
    public void setDayRateCents(int dayRateCents) { this.dayRateCents = dayRateCents; }
    public KitStatus getStatus() { return status; }
    public void setStatus(KitStatus status) { this.status = status; }
}
