package com.prescriptionservice.controllers;

import com.prescriptionservice.models.dto.*;
import com.prescriptionservice.services.ConsultationService;
import com.prescriptionservice.exceptions.ConsultationNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConsultationController.class)
class ConsultationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private ConsultationService consultationService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private QuestionsResponse mockQuestionsResponse;
    private EligibilityResponse mockEligibilityResponse;
    
    @BeforeEach
    void setUp() {
        List<QuestionDto> questions = Arrays.asList(
            new QuestionDto("Q1", "Do you experience nose itchiness when near pears?", "YES_NO", true),
            new QuestionDto("Q2", "Have you ever sneezed uncontrollably in a fruit market?", "YES_NO", true)
        );
        mockQuestionsResponse = new QuestionsResponse(questions, "consultation-123");
        
        mockEligibilityResponse = new EligibilityResponse(
            "consultation-123",
            true,
            "You are eligible for treatment",
            "ELIGIBLE"
        );
    }
    
    @Test
    void getQuestionsShouldReturnQuestionsResponse() throws Exception {
        // Given
        String productId = "pear-allergy";
        when(consultationService.getConsultationQuestions(productId)).thenReturn(mockQuestionsResponse);
        
        // When & Then
        mockMvc.perform(get("/api/consultations/{productId}/questions", productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.consultationId").value("consultation-123"))
                .andExpect(jsonPath("$.questions").isArray())
                .andExpect(jsonPath("$.questions.length()").value(2))
                .andExpect(jsonPath("$.questions[0].id").value("Q1"))
                .andExpect(jsonPath("$.questions[0].text").value("Do you experience nose itchiness when near pears?"));
    }
    
    @Test
    void submitAnswersShouldReturnEligibilityResponse() throws Exception {
        // Given
        List<AnswerDto> answers = Arrays.asList(
            new AnswerDto("Q1", "yes"),
            new AnswerDto("Q2", "yes")
        );
        ConsultationRequest request = new ConsultationRequest("John Doe", "1990-01-01", "123 Main St", answers);
        
        when(consultationService.processConsultation(eq("consultation-123"), any(ConsultationRequest.class)))
                .thenReturn(mockEligibilityResponse);
        
        // When & Then
        mockMvc.perform(post("/api/consultations/consultation-123/answers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.consultationId").value("consultation-123"))
                .andExpect(jsonPath("$.eligible").value(true))
                .andExpect(jsonPath("$.status").value("ELIGIBLE"))
                .andExpect(jsonPath("$.message").value("You are eligible for treatment"));
    }
    
    @Test
    void submitAnswersShouldReturnValidationErrorWhenInvalidRequest() throws Exception {
        // Given
        ConsultationRequest invalidRequest = new ConsultationRequest("", "", "", List.of());
        
        // When & Then
        mockMvc.perform(post("/api/consultations/consultation-123/answers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors").exists());
    }
    
    @Test
    void submitAnswersShouldReturnNotFoundWhenConsultationNotExists() throws Exception {
        // Given
        List<AnswerDto> answers = List.of(
                new AnswerDto("Q1", "yes")
        );
        ConsultationRequest request = new ConsultationRequest("John Doe", "1990-01-01", "123 Main St", answers);
        
        when(consultationService.processConsultation(eq("invalid-id"), any(ConsultationRequest.class)))
                .thenThrow(new ConsultationNotFoundException("invalid-id"));
        
        // When & Then
        mockMvc.perform(post("/api/consultations/invalid-id/answers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.consultationId").value("invalid-id"))
                .andExpect(jsonPath("$.eligible").value(false))
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Consultation not found: invalid-id"));
    }
}