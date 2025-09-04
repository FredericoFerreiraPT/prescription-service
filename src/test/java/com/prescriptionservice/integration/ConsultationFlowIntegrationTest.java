package com.prescriptionservice.integration;

import com.prescriptionservice.models.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test for the complete consultation flow from questions retrieval to eligibility assessment.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ConsultationFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void completeConsultationFlowShouldReturnEligibleForHighSeveritySymptoms() throws Exception {
        // Given - Get consultation questions first
        String productId = "pear-allergy";
        MvcResult questionsResult = mockMvc.perform(get("/api/consultations/{productId}/questions", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questions").isArray())
                .andExpect(jsonPath("$.questions.length()").value(5))
                .andExpect(jsonPath("$.consultationId").exists())
                .andReturn();
        
        String questionsJson = questionsResult.getResponse().getContentAsString();
        QuestionsResponse questionsResponse = objectMapper.readValue(questionsJson, QuestionsResponse.class);
        String consultationId = questionsResponse.getConsultationId();
        
        // Verify we have all expected questions
        assertNotNull(consultationId);
        assertEquals(5, questionsResponse.getQuestions().size());
        
        // When - Submit answers indicating high severity symptoms and no adverse reactions
        List<AnswerDto> answers = Arrays.asList(
            new AnswerDto("Q1", "yes"), // nose itchiness
            new AnswerDto("Q2", "yes"), // sneezing in fruit market
            new AnswerDto("Q3", "yes"), // scratchy throat
            new AnswerDto("Q4", "yes"), // avoid pear orchards
            new AnswerDto("Q5", "no")   // no adverse reaction to medication
        );
        
        ConsultationRequest request = new ConsultationRequest(
            "John Doe",
            "1990-01-01", 
            "123 Main Street, Test City, TC 12345",
            answers
        );
        
        // Then - Should return eligible status
        mockMvc.perform(post("/api/consultations/{consultationId}/answers", consultationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.consultationId").value(consultationId))
                .andExpect(jsonPath("$.eligible").value(true))
                .andExpect(jsonPath("$.status").value("ELIGIBLE"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("good candidate")));
    }
}