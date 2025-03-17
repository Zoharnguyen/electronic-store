package com.altech.electronic_store.dto;

import lombok.Data;
import jakarta.validation.constraints.Positive;

@Data
public class BasketItemDTO {
    private Long productId;
    
    @Positive(message = "Quantity must be greater than zero")
    private Integer quantity;
} 