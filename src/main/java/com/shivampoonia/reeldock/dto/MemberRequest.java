package com.shivampoonia.reeldock.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class MemberRequest {

    @NotBlank
    private String campusId;
    @NotBlank
    private String displayName;
    @Email
    @NotBlank
    private String mail;
    @NotBlank
    private String programme;
    @NotBlank
    private String cohort;

    public String getCampusId() { return campusId; }
    public void setCampusId(String campusId) { this.campusId = campusId; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
    public String getProgramme() { return programme; }
    public void setProgramme(String programme) { this.programme = programme; }
    public String getCohort() { return cohort; }
    public void setCohort(String cohort) { this.cohort = cohort; }
}
