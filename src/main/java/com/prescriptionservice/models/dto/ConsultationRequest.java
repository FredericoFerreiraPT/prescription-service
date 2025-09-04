package com.prescriptionservice.models.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * Data transfer object for consultation submission containing patient info and answers.
 */
public class ConsultationRequest {
    @NotNull(message = "Patient name is required")
    @NotBlank(message = "Patient name cannot be blank")
    @Size(max = 100, message = "Patient name must not exceed 100 characters")
    private String patientName;
    
    @NotNull(message = "Date of birth is required")
    @NotBlank(message = "Date of birth cannot be blank")
    private String dateOfBirth;
    
    @NotNull(message = "Address is required")
    @NotBlank(message = "Address cannot be blank")
    @Size(max = 200, message = "Address must not exceed 200 characters")
    private String address;
    
    @NotNull(message = "Answers are required")
    @NotEmpty(message = "At least one answer is required")
    @Valid
    private List<AnswerDto> answers;

    public ConsultationRequest() {}

    public ConsultationRequest(String patientName, String dateOfBirth, String address, List<AnswerDto> answers) {
        this.patientName = patientName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.answers = answers;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<AnswerDto> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDto> answers) {
        this.answers = answers;
    }
}