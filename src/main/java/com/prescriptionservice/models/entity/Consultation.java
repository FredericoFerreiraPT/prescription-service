package com.prescriptionservice.models.entity;

import java.util.List;
import java.util.UUID;

/**
 * Represents a consultation session containing patient answers and eligibility status.
 * Links to a Patient entity via patientId for proper data normalization.
 */
public class Consultation {
    private String id;
    private String patientId;
    private List<Answer> answers;
    private EligibilityStatus eligibilityStatus;

    public Consultation() {
        this.id = UUID.randomUUID().toString();
        this.eligibilityStatus = EligibilityStatus.PENDING;
    }

    public Consultation(String patientId) {
        this();
        this.patientId = patientId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public EligibilityStatus getEligibilityStatus() {
        return eligibilityStatus;
    }

    public void setEligibilityStatus(EligibilityStatus eligibilityStatus) {
        this.eligibilityStatus = eligibilityStatus;
    }

    public enum EligibilityStatus {
        PENDING,
        ELIGIBLE,
        NOT_ELIGIBLE,
        REQUIRES_REVIEW
    }
}