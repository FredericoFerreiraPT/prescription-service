package com.prescriptionservice.services;

import com.prescriptionservice.models.dto.*;
import com.prescriptionservice.models.entity.*;
import com.prescriptionservice.repositories.ConsultationRepository;
import com.prescriptionservice.repositories.QuestionRepository;
import com.prescriptionservice.repositories.ProductRepository;
import com.prescriptionservice.repositories.PatientRepository;
import com.prescriptionservice.exceptions.ConsultationNotFoundException;
import com.prescriptionservice.exceptions.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultationServiceTest {

    @Mock
    private QuestionRepository questionRepository;
    
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private PatientRepository patientRepository;
    
    @Mock
    private ConsultationRepository consultationRepository;
    
    @InjectMocks
    private ConsultationService consultationService;
    
    private List<Question> mockQuestions;
    private Product mockProduct;
    private Patient mockPatient;
    private Consultation mockConsultation;
    
    @BeforeEach
    void setUp() {
        mockQuestions = Arrays.asList(
            new Question("Q1", "Do you experience nose itchiness when near pears?", Question.QuestionType.YES_NO, true),
            new Question("Q2", "Have you ever sneezed uncontrollably in a fruit market?", Question.QuestionType.YES_NO, true)
        );
        
        mockProduct = new Product(
            "pear-allergy", 
            "Pear Allergy Treatment", 
            "Treatment for pear allergies", 
            Arrays.asList("Q1", "Q2")
        );
        
        mockPatient = new Patient(
            "patient-123",
            "John Doe",
            "1990-01-01",
            "123 Main Street, Test City, TC 12345"
        );
        
        mockConsultation = new Consultation("patient-123");
        mockConsultation.setId("consultation-123");
    }
    
    @Test
    void getConsultationQuestionsShouldReturnQuestionsAndCreateConsultation() {
        // Given
        String productId = "pear-allergy";
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(questionRepository.findByIds(mockProduct.getQuestionIds())).thenReturn(mockQuestions);
        when(consultationRepository.save(any(Consultation.class))).thenReturn(mockConsultation);
        
        // When
        QuestionsResponse response = consultationService.getConsultationQuestions(productId);
        
        // Then
        assertNotNull(response);
        assertEquals(2, response.getQuestions().size());
        assertNotNull(response.getConsultationId());
        
        verify(productRepository).findById(productId);
        verify(questionRepository).findByIds(mockProduct.getQuestionIds());
        verify(consultationRepository).save(any(Consultation.class));
    }
    
    @Test
    void processConsultationShouldReturnEligibleWhenAllSymptomsPresent() {
        // Given
        String consultationId = "consultation-123";
        List<AnswerDto> answers = Arrays.asList(
            new AnswerDto("Q1", "yes"),
            new AnswerDto("Q2", "yes"),
            new AnswerDto("Q3", "yes"),
            new AnswerDto("Q5", "no")
        );
        ConsultationRequest request = new ConsultationRequest("John Doe", "1990-01-01", "123 Main St", answers);
        
        when(consultationRepository.findById(consultationId)).thenReturn(Optional.of(mockConsultation));
        when(patientRepository.findById("patient-123")).thenReturn(Optional.of(mockPatient));
        when(consultationRepository.save(any(Consultation.class))).thenReturn(mockConsultation);
        
        // When
        EligibilityResponse response = consultationService.processConsultation(consultationId, request);
        
        // Then
        assertNotNull(response);
        assertEquals(consultationId, response.getConsultationId());
        assertTrue(response.isEligible());
        assertEquals("ELIGIBLE", response.getStatus());
        assertTrue(response.getMessage().contains("good candidate"));
        
        verify(consultationRepository).findById(consultationId);
        verify(consultationRepository).save(any(Consultation.class));
    }
    
    @Test
    void processConsultationShouldReturnNotEligibleWhenAdverseReaction() {
        // Given
        String consultationId = "consultation-123";
        List<AnswerDto> answers = Arrays.asList(
            new AnswerDto("Q1", "yes"),
            new AnswerDto("Q5", "yes")
        );
        ConsultationRequest request = new ConsultationRequest("John Doe", "1990-01-01", "123 Main St", answers);
        
        when(consultationRepository.findById(consultationId)).thenReturn(Optional.of(mockConsultation));
        when(patientRepository.findById("patient-123")).thenReturn(Optional.of(mockPatient));
        when(consultationRepository.save(any(Consultation.class))).thenReturn(mockConsultation);
        
        // When
        EligibilityResponse response = consultationService.processConsultation(consultationId, request);
        
        // Then
        assertNotNull(response);
        assertEquals(consultationId, response.getConsultationId());
        assertFalse(response.isEligible());
        assertEquals("NOT_ELIGIBLE", response.getStatus());
        assertTrue(response.getMessage().contains("adverse reaction"));
    }
    
    @Test
    void processConsultationShouldReturnRequiresReviewWhenPartialSymptoms() {
        // Given
        String consultationId = "consultation-123";
        List<AnswerDto> answers = Arrays.asList(
            new AnswerDto("Q1", "yes"),
            new AnswerDto("Q2", "no"),
            new AnswerDto("Q3", "no"),
            new AnswerDto("Q5", "no")
        );
        ConsultationRequest request = new ConsultationRequest("John Doe", "1990-01-01", "123 Main St", answers);
        
        when(consultationRepository.findById(consultationId)).thenReturn(Optional.of(mockConsultation));
        when(patientRepository.findById("patient-123")).thenReturn(Optional.of(mockPatient));
        when(consultationRepository.save(any(Consultation.class))).thenReturn(mockConsultation);
        
        // When
        EligibilityResponse response = consultationService.processConsultation(consultationId, request);
        
        // Then
        assertNotNull(response);
        assertTrue(response.isEligible());
        assertEquals("REQUIRES_REVIEW", response.getStatus());
        assertTrue(response.getMessage().contains("doctor review"));
    }
    
    @Test
    void processConsultationShouldReturnNotEligibleWhenMinimalSymptoms() {
        // Given
        String consultationId = "consultation-123";
        List<AnswerDto> answers = Arrays.asList(
            new AnswerDto("Q1", "no"),
            new AnswerDto("Q2", "no"),
            new AnswerDto("Q3", "no"),
            new AnswerDto("Q5", "no")
        );
        ConsultationRequest request = new ConsultationRequest("John Doe", "1990-01-01", "123 Main St", answers);
        
        when(consultationRepository.findById(consultationId)).thenReturn(Optional.of(mockConsultation));
        when(patientRepository.findById("patient-123")).thenReturn(Optional.of(mockPatient));
        when(consultationRepository.save(any(Consultation.class))).thenReturn(mockConsultation);
        
        // When
        EligibilityResponse response = consultationService.processConsultation(consultationId, request);
        
        // Then
        assertNotNull(response);
        assertFalse(response.isEligible());
        assertEquals("NOT_ELIGIBLE", response.getStatus());
        assertTrue(response.getMessage().contains("may not be necessary"));
    }
    
    @Test
    void getConsultationQuestionsShouldThrowExceptionWhenProductNotFound() {
        // Given
        String productId = "invalid-product";
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        
        // When & Then
        ProductNotFoundException exception = assertThrows(
            ProductNotFoundException.class,
            () -> consultationService.getConsultationQuestions(productId)
        );
        
        assertTrue(exception.getMessage().contains("Product not found"));
        verify(productRepository).findById(productId);
        verify(questionRepository, never()).findByIds(any());
        verify(consultationRepository, never()).save(any(Consultation.class));
    }
    
    @Test
    void processConsultationShouldThrowExceptionWhenConsultationNotFound() {
        // Given
        String consultationId = "invalid-id";
        ConsultationRequest request = new ConsultationRequest("John Doe", "1990-01-01", "123 Main St", Arrays.asList());
        
        when(consultationRepository.findById(consultationId)).thenReturn(Optional.empty());
        
        // When & Then
        ConsultationNotFoundException exception = assertThrows(
            ConsultationNotFoundException.class,
            () -> consultationService.processConsultation(consultationId, request)
        );
        
        assertTrue(exception.getMessage().contains("Consultation not found"));
        verify(consultationRepository).findById(consultationId);
        verify(consultationRepository, never()).save(any(Consultation.class));
    }
}