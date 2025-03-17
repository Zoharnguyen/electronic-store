package com.altech.electronic_store.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class DiscountDTO {
    private Long id;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    @Positive(message = "Minimum quantity must be greater than zero")
    private Integer minimumQuantity;
    
    @Positive(message = "Discount percentage must be greater than zero")
    private BigDecimal discountPercentage;
    
    private String description;
} 