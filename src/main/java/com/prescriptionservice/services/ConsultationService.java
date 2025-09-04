package com.prescriptionservice.services;

import com.prescriptionservice.models.dto.*;
import com.prescriptionservice.models.entity.*;
import com.prescriptionservice.repositories.ConsultationRepository;
import com.prescriptionservice.repositories.QuestionRepository;
import com.prescriptionservice.repositories.ProductRepository;
import com.prescriptionservice.repositories.PatientRepository;
import com.prescriptionservice.exceptions.ConsultationNotFoundException;
import com.prescriptionservice.exceptions.ConsultationAccessException;
import com.prescriptionservice.exceptions.PatientNotFoundException;
import com.prescriptionservice.exceptions.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class handling consultation business logic including eligibility assessment.
 * Contains the core logic for determining patient eligibility based on consultation answers.
 */
@Service
public class ConsultationService {
    
    private final QuestionRepository questionRepository;
    private final ProductRepository productRepository;
    private final PatientRepository patientRepository;
    private final ConsultationRepository consultationRepository;
    
    @Autowired
    public ConsultationService(QuestionRepository questionRepository, ProductRepository productRepository, PatientRepository patientRepository, ConsultationRepository consultationRepository) {
        this.questionRepository = questionRepository;
        this.productRepository = productRepository;
        this.patientRepository = patientRepository;
        this.consultationRepository = consultationRepository;
    }
    
    /**
     * Retrieves all consultation questions available for patients for a specific product.
     * Creates a new consultation session for tracking purposes.
     * 
     * @param productId The product ID to retrieve questions for
     * @return QuestionsResponse containing product-specific questions and consultation ID
     */
    public QuestionsResponse getConsultationQuestions(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        
        List<Question> questions = questionRepository.findByIds(product.getQuestionIds());
        List<QuestionDto> questionDtos = questions.stream()
                .map(QuestionDto::fromEntity)
                .collect(Collectors.toList());
        
        // Create a new consultation session linked to patient
        // TODO: Extract patientId from JWT token instead of hardcoding
        String patientId = "patient-123";
        Consultation consultation = new Consultation(patientId);
        consultationRepository.save(consultation);
        
        return new QuestionsResponse(questionDtos, consultation.getId());
    }
    
    /**
     * Processes consultation answers and determines patient eligibility.
     * Implements business rules for prescription eligibility assessment.
     * 
     * @param consultationId The consultation session ID
     * @param request The consultation request containing patient info and answers
     * @return EligibilityResponse indicating eligibility status and reasoning
     */
    public EligibilityResponse processConsultation(String consultationId, ConsultationRequest request) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ConsultationNotFoundException(consultationId));
        
        // TODO: Extract patientId from JWT token instead of hardcoding
        String patientId = "patient-123";
        
        // Verify patient exists (would normally get patientId from auth token)
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));

        // TODO: Here we would extract patient data from the retrieved Patient, rather than accepting it in the request

        // Ensure consultation belongs to the authenticated patient
        if (!patientId.equals(consultation.getPatientId())) {
            throw new ConsultationAccessException(patientId, consultationId);
        }
        
        List<Answer> answers = request.getAnswers().stream()
                .map(dto -> new Answer(dto.getQuestionId(), dto.getValue()))
                .collect(Collectors.toList());
        consultation.setAnswers(answers);
        
        // Assess eligibility based on answers
        EligibilityAssessment assessment = assessEligibility(answers);
        consultation.setEligibilityStatus(assessment.status());
        
        consultationRepository.save(consultation);
        
        return new EligibilityResponse(
                consultationId,
                assessment.eligible(),
                assessment.message(),
                assessment.status().name()
        );
    }
    
    /**
     * Assesses patient eligibility based on consultation answers using business rules.
     * Current rules focus on adverse medication reactions and severe allergy symptoms.
     * This method is designed to be easily extensible for additional rules.
     */
    private EligibilityAssessment assessEligibility(List<Answer> answers) {
        // Rule 1: Check for previous adverse reactions to medication (Q5)
        boolean hasAdverseReaction = answers.stream()
                .filter(answer -> "Q5".equals(answer.getQuestionId()))
                .findFirst()
                .map(answer -> "yes".equalsIgnoreCase(answer.getValue()))
                .orElse(false);
        
        if (hasAdverseReaction) {
            return new EligibilityAssessment(
                    false,
                    "Unfortunately, we cannot prescribe medication due to your previous adverse reaction to allergy medication. Please consult with your doctor.",
                    Consultation.EligibilityStatus.NOT_ELIGIBLE
            );
        }
        
        // Rule 2: Count severe allergy symptoms (Q1, Q2, Q3)
        long severeSymptomCount = answers.stream()
                .filter(answer -> List.of("Q1", "Q2", "Q3").contains(answer.getQuestionId()))
                .filter(answer -> "yes".equalsIgnoreCase(answer.getValue()))
                .count();
        
        if (severeSymptomCount >= 3) {
            return new EligibilityAssessment(
                    true,
                    "Great news! Based on your symptoms, you appear to be a good candidate for our pear allergy medication. We'll proceed with your consultation.",
                    Consultation.EligibilityStatus.ELIGIBLE
            );
        } else if (severeSymptomCount >= 1) {
            return new EligibilityAssessment(
                    true,
                    "Based on your responses, you may benefit from our medication. We'll have a doctor review your case.",
                    Consultation.EligibilityStatus.REQUIRES_REVIEW
            );
        }
        
        // Default case: minimal symptoms
        return new EligibilityAssessment(
                false,
                "Based on your responses, our medication may not be necessary for your current symptom level. Consider consulting with your doctor for alternative treatments.",
                Consultation.EligibilityStatus.NOT_ELIGIBLE
        );
    }

    /**
         * Helper class to encapsulate eligibility assessment results.
         */
        private record EligibilityAssessment(boolean eligible, String message, Consultation.EligibilityStatus status) {
    }
}