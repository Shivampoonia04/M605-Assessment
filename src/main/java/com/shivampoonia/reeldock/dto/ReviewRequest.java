package com.shivampoonia.reeldock.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewRequest {

    @NotNull
    private Long productionId;
    @NotNull
    private Long memberId;
    @Min(1)
    @Max(5)
    private int stars;
    @NotBlank
    private String body;

    public Long getProductionId() { return productionId; }
    public void setProductionId(Long productionId) { this.productionId = productionId; }
    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public int getStars() { return stars; }
    public void setStars(int stars) { this.stars = stars; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
}
