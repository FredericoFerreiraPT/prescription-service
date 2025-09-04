package com.prescriptionservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a product is not found by its ID.
 * Results in HTTP 404 Not Found response.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {
    
    public ProductNotFoundException(String productId) {
        super("Product not found: " + productId);
    }
    
    public ProductNotFoundException(String productId, Throwable cause) {
        super("Product not found: " + productId, cause);
    }
}