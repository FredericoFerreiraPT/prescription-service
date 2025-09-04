package com.prescriptionservice.repositories;

import com.prescriptionservice.models.entity.Product;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of ProductRepository using HashMap storage.
 * Initializes with predefined products and their associated question IDs.
 */
@Repository
public class ProductRepositoryImpl implements ProductRepository {
    
    private final Map<String, Product> products = new ConcurrentHashMap<>();
    private static final String PEAR_ALLERGY_PRODUCT_ID = "pear-allergy";
    
    @PostConstruct
    public void initializeProducts() {
        List<String> pearAllergyQuestionIds = Arrays.asList("Q1", "Q2", "Q3", "Q4", "Q5");
        
        Product pearAllergyProduct = new Product(
            PEAR_ALLERGY_PRODUCT_ID,
            "Pear Allergy Treatment",
            "Treatment for allergic reactions to pears and pear-derived products",
            pearAllergyQuestionIds
        );
        
        products.put(PEAR_ALLERGY_PRODUCT_ID, pearAllergyProduct);
    }
    
    @Override
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }
}