package com.prescriptionservice.models.entity;

/**
 * Represents a patient in the system who can have multiple consultations.
 * Contains basic patient information and serves as foundation for future payment details storage.
 */
public class Patient {
    private String id;
    private String name;
    private String dateOfBirth;
    private String address;

    public Patient() {}

    public Patient(String id, String name, String dateOfBirth, String address) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
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
}