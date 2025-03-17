package com.altech.electronic_store.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ReceiptItemDTO {
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private BigDecimal discountedPrice;
} 