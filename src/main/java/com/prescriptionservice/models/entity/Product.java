package com.prescriptionservice.models.entity;

import java.util.List;

/**
 * Represents a medical product that has associated consultation questions.
 * This allows for reusable questions across multiple products and better data normalization.
 */
public class Product {
    private String id;
    private String name;
    private String description;
    private List<String> questionIds;

    public Product() {}

    public Product(String id, String name, String description, List<String> questionIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.questionIds = questionIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<String> questionIds) {
        this.questionIds = questionIds;
    }
}