package com.prescriptionservice.models.entity;

/**
 * Represents a reusable consultation question that can be associated with multiple products.
 */
public class Question {
    private String id;
    private String text;
    private QuestionType type;
    private boolean required;

    public Question() {}

    public Question(String id, String text, QuestionType type, boolean required) {
        this.id = id;
        this.text = text;
        this.type = type;
        this.required = required;
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

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public enum QuestionType {
        YES_NO,
        TEXT,
        MULTIPLE_CHOICE
    }
}