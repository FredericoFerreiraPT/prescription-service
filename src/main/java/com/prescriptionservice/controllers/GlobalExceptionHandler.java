package com.prescriptionservice.controllers;

import com.prescriptionservice.exceptions.ConsultationNotFoundException;
import com.prescriptionservice.exceptions.ConsultationAccessException;
import com.prescriptionservice.exceptions.PatientNotFoundException;
import com.prescriptionservice.exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for consistent error responses across the application.
 * Handles validation errors and other common exceptions.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handles validation errors from request body validation.
     * 
     * @param ex The validation exception
     * @return ResponseEntity with validation error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        response.put("message", "Validation failed");
        response.put("errors", errors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * Handles consultation not found exceptions.
     * Returns response in EligibilityResponse format for API consistency.
     * 
     * @param ex The consultation not found exception
     * @return ResponseEntity with 404 Not Found status
     */
    @ExceptionHandler(ConsultationNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleConsultationNotFoundException(ConsultationNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        
        response.put("consultationId", ex.getConsultationId());
        response.put("eligible", false);
        response.put("status", "ERROR");
        response.put("message", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    /**
     * Handles product not found exceptions.
     * 
     * @param ex The product not found exception
     * @return ResponseEntity with 404 Not Found status
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProductNotFoundException(ProductNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("status", "error");
        response.put("error_type", "not_found");
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    /**
     * Handles patient not found exceptions.
     * 
     * @param ex The patient not found exception
     * @return ResponseEntity with 404 Not Found status
     */
    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePatientNotFoundException(PatientNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("status", "error");
        response.put("error_type", "not_found");
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    /**
     * Handles consultation access exceptions when patient tries to access consultation they don't own.
     * 
     * @param ex The consultation access exception
     * @return ResponseEntity with 403 Forbidden status
     */
    @ExceptionHandler(ConsultationAccessException.class)
    public ResponseEntity<Map<String, Object>> handleConsultationAccessException(ConsultationAccessException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("status", "forbidden");
        response.put("error_type", "access_denied");
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
    
    /**
     * Handles general illegal argument exceptions.
     * 
     * @param ex The illegal argument exception
     * @return ResponseEntity with error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("status", "error");
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * Handles unexpected server errors.
     * 
     * @param ex The general exception
     * @return ResponseEntity with generic error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "An unexpected error occurred. Please try again.");
        response.put("status", "error");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}