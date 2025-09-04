package com.prescriptionservice.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data transfer object for patient answers received from the frontend.
 */
public class AnswerDto {
    @NotNull(message = "Question ID is required")
    @NotBlank(message = "Question ID cannot be blank")
    private String questionId;
    
    @NotNull(message = "Answer value is required")
    @NotBlank(message = "Answer value cannot be blank")
    private String value;

    public AnswerDto() {}

    public AnswerDto(String questionId, String value) {
        this.questionId = questionId;
        this.value = value;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}