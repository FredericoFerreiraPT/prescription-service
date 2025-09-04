package com.prescriptionservice.repositories;

import com.prescriptionservice.models.entity.Consultation;
import java.util.Optional;

/**
 * Repository interface for managing consultation data.
 * Provides abstraction for potential future database integration.
 */
public interface ConsultationRepository {
    
    /**
     * Saves a consultation to storage.
     * 
     * @param consultation The consultation to save
     * @return The saved consultation
     */
    Consultation save(Consultation consultation);
    
    /**
     * Finds a consultation by its ID.
     * 
     * @param id The consultation ID
     * @return Optional containing the consultation if found
     */
    Optional<Consultation> findById(String id);
}