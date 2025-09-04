package com.prescriptionservice.repositories;

import com.prescriptionservice.models.entity.Patient;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of PatientRepository using HashMap storage.
 * Initializes with a single hardcoded patient for testing purposes.
 */
@Repository
public class PatientRepositoryImpl implements PatientRepository {
    
    private final Map<String, Patient> patients = new ConcurrentHashMap<>();
    private static final String DEFAULT_PATIENT_ID = "patient-123";
    
    @PostConstruct
    public void initializePatients() {
        Patient defaultPatient = new Patient(
            DEFAULT_PATIENT_ID,
            "John Doe",
            "1990-01-01",
            "123 Main Street, Test City, TC 12345"
        );
        
        patients.put(DEFAULT_PATIENT_ID, defaultPatient);
    }
    
    @Override
    public Optional<Patient> findById(String id) {
        return Optional.ofNullable(patients.get(id));
    }
    
    @Override
    public Patient save(Patient patient) {
        patients.put(patient.getId(), patient);
        return patient;
    }
}