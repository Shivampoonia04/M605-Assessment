package com.shivampoonia.reeldock.dto;

import com.shivampoonia.reeldock.model.KitKind;
import com.shivampoonia.reeldock.model.KitStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class KitRequest {

    @NotBlank
    private String tag;
    @NotBlank
    private String label;
    @NotNull
    private KitKind kind;
    @NotBlank
    private String serial;
    @NotBlank
    private String bay;
    @Min(0)
    private int dayRateCents;
    private KitStatus status = KitStatus.READY;

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
