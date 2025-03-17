package com.altech.electronic_store.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ReceiptDTO {
    private List<ReceiptItemDTO> items;
    private List<String> appliedDiscounts;
    private BigDecimal totalPrice;
    private BigDecimal totalDiscount;
    private BigDecimal finalPrice;
} 