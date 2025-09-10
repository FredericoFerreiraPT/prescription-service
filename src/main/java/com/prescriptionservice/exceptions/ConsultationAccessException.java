package com.prescriptionservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a patient attempts to access a consultation they don't own.
 * Results in HTTP 403 Forbidden response.
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ConsultationAccessException extends RuntimeException {
    
    private final String patientId;
    private final String consultationId;
    
    public ConsultationAccessException(String patientId, String consultationId) {
        super("Patient " + patientId + " is not authorized to access consultation " + consultationId);
        this.patientId = patientId;
        this.consultationId = consultationId;
    }
    
    public ConsultationAccessException(String patientId, String consultationId, Throwable cause) {
        super("Patient " + patientId + " is not authorized to access consultation " + consultationId, cause);
        this.patientId = patientId;
        this.consultationId = consultationId;
    }
    
    public String getPatientId() {
        return patientId;
    }
    
    public String getConsultationId() {
        return consultationId;
    }
}