package com.prescriptionservice.models.dto;

/**
 * Data transfer object for eligibility assessment response sent to the frontend.
 */
public class EligibilityResponse {
    private String consultationId;
    private boolean eligible;
    private String message;
    private String status;

    public EligibilityResponse() {}

    public EligibilityResponse(String consultationId, boolean eligible, String message, String status) {
        this.consultationId = consultationId;
        this.eligible = eligible;
        this.message = message;
        this.status = status;
    }

    public String getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(String consultationId) {
        this.consultationId = consultationId;
    }

    public boolean isEligible() {
        return eligible;
    }

    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}