package com.prescriptionservice.models.dto;

import java.util.List;

/**
 * Data transfer object containing all consultation questions for the frontend.
 */
public class QuestionsResponse {
    private List<QuestionDto> questions;
    private String consultationId;

    public QuestionsResponse() {}

    public QuestionsResponse(List<QuestionDto> questions, String consultationId) {
        this.questions = questions;
        this.consultationId = consultationId;
    }

    public List<QuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDto> questions) {
        this.questions = questions;
    }

    public String getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(String consultationId) {
        this.consultationId = consultationId;
    }
}