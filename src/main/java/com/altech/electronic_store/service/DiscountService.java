package com.altech.electronic_store.service;

import com.altech.electronic_store.dto.DiscountDTO;
import com.altech.electronic_store.exception.ResourceNotFoundException;
import com.altech.electronic_store.model.Discount;
import com.altech.electronic_store.model.Product;
import com.altech.electronic_store.repository.DiscountRepository;
import com.altech.electronic_store.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountService {
    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;

    @Transactional
    public DiscountDTO createDiscount(DiscountDTO discountDTO) {
        Product product = productRepository.findById(discountDTO.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + discountDTO.getProductId()));

        Discount discount = new Discount();
        discount.setProduct(product);
        discount.setMinimumQuantity(discountDTO.getMinimumQuantity());
        discount.setDiscountPercentage(discountDTO.getDiscountPercentage());
        discount.setDescription(discountDTO.getDescription());

        Discount savedDiscount = discountRepository.save(discount);
        return convertToDTO(savedDiscount);
    }

    @Transactional(readOnly = true)
    public List<DiscountDTO> getDiscountsByProduct(Long productId) {
        return discountRepository.findByProductId(productId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    private DiscountDTO convertToDTO(Discount discount) {
        DiscountDTO dto = new DiscountDTO();
        dto.setId(discount.getId());
        dto.setProductId(discount.getProduct().getId());
        dto.setMinimumQuantity(discount.getMinimumQuantity());
        dto.setDiscountPercentage(discount.getDiscountPercentage());
        dto.setDescription(discount.getDescription());
        return dto;
    }
} 