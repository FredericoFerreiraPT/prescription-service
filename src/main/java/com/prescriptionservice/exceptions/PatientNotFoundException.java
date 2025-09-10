package com.prescriptionservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a patient is not found by their ID.
 * Results in HTTP 404 Not Found response.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PatientNotFoundException extends RuntimeException {
    
    private final String patientId;
    
    public PatientNotFoundException(String patientId) {
        super("Patient not found: " + patientId);
        this.patientId = patientId;
    }
    
    public PatientNotFoundException(String patientId, Throwable cause) {
        super("Patient not found: " + patientId, cause);
        this.patientId = patientId;
    }
    
    public String getPatientId() {
        return patientId;
    }
}