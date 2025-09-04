package com.prescriptionservice.models.dto;

import com.prescriptionservice.models.entity.Question;

/**
 * Data transfer object for consultation questions sent to the frontend.
 */
public class QuestionDto {
    private String id;
    private String text;
    private String type;
    private boolean required;

    public QuestionDto() {}

    public QuestionDto(String id, String text, String type, boolean required) {
        this.id = id;
        this.text = text;
        this.type = type;
        this.required = required;
    }

    public static QuestionDto fromEntity(Question question) {
        return new QuestionDto(
            question.getId(),
            question.getText(),
            question.getType().name(),
            question.isRequired()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}