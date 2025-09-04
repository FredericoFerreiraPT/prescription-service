package com.prescriptionservice.repositories;

import com.prescriptionservice.models.entity.Question;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing consultation questions.
 * Provides abstraction for potential future database integration.
 */
@Repository
public interface QuestionRepository {
    
    /**
     * Retrieves multiple questions by their IDs.
     * 
     * @param questionIds List of question IDs to retrieve
     * @return List of questions matching the provided IDs
     */
    List<Question> findByIds(List<String> questionIds);
    
    /**
     * Retrieves all available consultation questions.
     * 
     * @return List of all questions
     */
    List<Question> findAll();
    
    /**
     * Finds a specific question by its ID.
     * 
     * @param id The question ID
     * @return Optional containing the question if found
     */
    Optional<Question> findById(String id);
}