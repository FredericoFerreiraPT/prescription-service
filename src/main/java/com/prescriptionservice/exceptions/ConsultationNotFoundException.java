package com.prescriptionservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a consultation is not found by its ID.
 * Results in HTTP 404 Not Found response.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ConsultationNotFoundException extends RuntimeException {
    
    private final String consultationId;
    
    public ConsultationNotFoundException(String consultationId) {
        super("Consultation not found: " + consultationId);
        this.consultationId = consultationId;
    }
    
    public ConsultationNotFoundException(String consultationId, Throwable cause) {
        super("Consultation not found: " + consultationId, cause);
        this.consultationId = consultationId;
    }
    
    public String getConsultationId() {
        return consultationId;
    }
}