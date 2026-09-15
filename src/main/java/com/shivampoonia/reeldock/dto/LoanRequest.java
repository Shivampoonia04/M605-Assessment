package com.shivampoonia.reeldock.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class LoanRequest {

    @NotNull
    private Long memberId;
    @NotNull
    private Long kitId;
    @NotNull
    private LocalDateTime takenAt;
    @NotNull
    private LocalDateTime dueAt;
    private String note;

    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public Long getKitId() { return kitId; }
    public void setKitId(Long kitId) { this.kitId = kitId; }
    public LocalDateTime getTakenAt() { return takenAt; }
    public void setTakenAt(LocalDateTime takenAt) { this.takenAt = takenAt; }
    public LocalDateTime getDueAt() { return dueAt; }
    public void setDueAt(LocalDateTime dueAt) { this.dueAt = dueAt; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
