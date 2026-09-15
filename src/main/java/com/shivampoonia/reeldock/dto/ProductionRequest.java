package com.shivampoonia.reeldock.dto;

import com.shivampoonia.reeldock.model.CutStatus;
import com.shivampoonia.reeldock.model.ProductionKind;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public class ProductionRequest {

    @NotBlank
    private String slug;
    @NotBlank
    private String title;
    @NotNull
    private ProductionKind kind;
    @NotNull
    private Long ownerId;
    private Long kitId;
    @Min(0)
    private int runtimeSec;
    private CutStatus cutStatus = CutStatus.DRAFT;
    private Map<String, Object> payload;

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public ProductionKind getKind() { return kind; }
    public void setKind(ProductionKind kind) { this.kind = kind; }
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
    public Long getKitId() { return kitId; }
    public void setKitId(Long kitId) { this.kitId = kitId; }
    public int getRuntimeSec() { return runtimeSec; }
    public void setRuntimeSec(int runtimeSec) { this.runtimeSec = runtimeSec; }
    public CutStatus getCutStatus() { return cutStatus; }
    public void setCutStatus(CutStatus cutStatus) { this.cutStatus = cutStatus; }
    public Map<String, Object> getPayload() { return payload; }
    public void setPayload(Map<String, Object> payload) { this.payload = payload; }
}
