package com.prescriptionservice.repositories;

import com.prescriptionservice.models.entity.Consultation;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of ConsultationRepository using HashMap storage.
 * Thread-safe implementation suitable for concurrent access.
 */
@Repository
public class ConsultationRepositoryImpl implements ConsultationRepository {
    
    private final Map<String, Consultation> consultations = new ConcurrentHashMap<>();
    
    @Override
    public Consultation save(Consultation consultation) {
        consultations.put(consultation.getId(), consultation);
        return consultation;
    }
    
    @Override
    public Optional<Consultation> findById(String id) {
        return Optional.ofNullable(consultations.get(id));
    }
}