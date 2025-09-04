package com.prescriptionservice.repositories;

import com.prescriptionservice.models.entity.Product;
import java.util.Optional;

/**
 * Repository interface for managing product data and their associated questions.
 * Provides abstraction for potential future database integration.
 */
public interface ProductRepository {
    
    /**
     * Finds a product by its ID.
     * 
     * @param id The product ID
     * @return Optional containing the product if found
     */
    Optional<Product> findById(String id);
}