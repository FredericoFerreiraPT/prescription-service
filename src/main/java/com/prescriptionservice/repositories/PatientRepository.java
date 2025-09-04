package com.prescriptionservice.repositories;

import com.prescriptionservice.models.entity.Patient;
import java.util.Optional;

/**
 * Repository interface for managing patient data.
 * Provides abstraction for potential future database integration.
 */
public interface PatientRepository {
    
    /**
     * Finds a patient by their ID.
     * 
     * @param id The patient ID
     * @return Optional containing the patient if found
     */
    Optional<Patient> findById(String id);
    
    /**
     * Saves a patient to storage.
     * 
     * @param patient The patient to save
     * @return The saved patient
     */
    Patient save(Patient patient);
}