package com.prescriptionservice.controllers;

import com.prescriptionservice.models.dto.ConsultationRequest;
import com.prescriptionservice.models.dto.EligibilityResponse;
import com.prescriptionservice.models.dto.QuestionsResponse;
import com.prescriptionservice.services.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * REST controller handling consultation-related endpoints.
 * Provides APIs for retrieving questions and submitting consultation answers.
 */
@RestController
@RequestMapping("/api/consultations")
@Validated
public class ConsultationController {
    
    private final ConsultationService consultationService;
    
    @Autowired
    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }
    
    /**
     * Retrieves all consultation questions for patient assessment for a specific product.
     * Creates a new consultation session and returns available questions.
     * 
     * @param productId The product ID to retrieve questions for
     * @return ResponseEntity containing product-specific questions and consultation ID
     */
    @GetMapping("/{productId}/questions")
    public ResponseEntity<QuestionsResponse> getQuestions(@PathVariable String productId) {
        QuestionsResponse response = consultationService.getConsultationQuestions(productId);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Processes consultation answers and determines patient eligibility.
     * Validates input data and returns eligibility assessment.
     * 
     * @param consultationId The consultation session ID
     * @param request The consultation request containing patient info and answers
     * @return ResponseEntity containing eligibility determination
     */
    @PostMapping("/{consultationId}/answers")
    public ResponseEntity<EligibilityResponse> submitAnswers(
            @PathVariable String consultationId,
            @Valid @RequestBody ConsultationRequest request) {
        
        EligibilityResponse response = consultationService.processConsultation(consultationId, request);
        return ResponseEntity.ok(response);
    }
}