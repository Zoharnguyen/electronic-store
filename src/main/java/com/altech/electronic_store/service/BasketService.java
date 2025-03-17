package com.altech.electronic_store.service;

import com.altech.electronic_store.dto.*;
import com.altech.electronic_store.exception.ResourceNotFoundException;
import com.altech.electronic_store.model.*;
import com.altech.electronic_store.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasketService {
    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;

    @Transactional
    public Long createBasket() {
        Basket basket = new Basket();
        return basketRepository.save(basket).getId();
    }

    @Transactional
    public void addItem(Long basketId, BasketItemDTO itemDTO) {
        Basket basket = basketRepository.findWithLockingById(basketId)
            .orElseThrow(() -> new ResourceNotFoundException("Basket not found with id: " + basketId));

        Product product = productRepository.findWithLockingById(itemDTO.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemDTO.getProductId()));

        if (product.getStockQuantity() < itemDTO.getQuantity()) {
            throw new IllegalStateException("Insufficient stock for product: " + product.getName());
        }

        Optional<BasketItem> existingItem = basket.getItems().stream()
            .filter(item -> item.getProduct().getId().equals(itemDTO.getProductId()))
            .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + itemDTO.getQuantity());
        } else {
            BasketItem newItem = new BasketItem();
            newItem.setProduct(product);
            newItem.setQuantity(itemDTO.getQuantity());
            basket.getItems().add(newItem);
        }

        product.setStockQuantity(product.getStockQuantity() - itemDTO.getQuantity());
        basketRepository.save(basket);
    }

    @Transactional
    public void removeItem(Long basketId, Long productId, Integer quantity) {
        Basket basket = basketRepository.findWithLockingById(basketId)
            .orElseThrow(() -> new ResourceNotFoundException("Basket not found with id: " + basketId));

        BasketItem item = basket.getItems().stream()
            .filter(i -> i.getProduct().getId().equals(productId))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Product not found in basket"));

        Product product = item.getProduct();
        
        if (quantity >= item.getQuantity()) {
            basket.getItems().remove(item);
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
        } else {
            item.setQuantity(item.getQuantity() - quantity);
            product.setStockQuantity(product.getStockQuantity() + quantity);
        }

        basketRepository.save(basket);
    }

    @Transactional(readOnly = true)
    public ReceiptDTO calculateReceipt(Long basketId) {
        Basket basket = basketRepository.findById(basketId)
            .orElseThrow(() -> new ResourceNotFoundException("Basket not found with id: " + basketId));

        ReceiptDTO receipt = new ReceiptDTO();
        List<ReceiptItemDTO> items = new ArrayList<>();
        List<String> appliedDiscounts = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;

        for (BasketItem item : basket.getItems()) {
            ReceiptItemDTO receiptItem = new ReceiptItemDTO();
            receiptItem.setProductName(item.getProduct().getName());
            receiptItem.setQuantity(item.getQuantity());
            receiptItem.setUnitPrice(item.getProduct().getPrice());
            
            BigDecimal itemTotal = item.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()));
            receiptItem.setTotalPrice(itemTotal);

            // Apply discounts
            List<Discount> discounts = discountRepository.findByProductId(item.getProduct().getId());
            BigDecimal maxDiscount = BigDecimal.ZERO;
            String appliedDiscount = null;

            for (Discount discount : discounts) {
                if (item.getQuantity() >= discount.getMinimumQuantity()) {
                    BigDecimal discountAmount = itemTotal
                        .multiply(discount.getDiscountPercentage())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    if (discountAmount.compareTo(maxDiscount) > 0) {
                        maxDiscount = discountAmount;
                        appliedDiscount = discount.getDescription();
                    }
                }
            }

            if (maxDiscount.compareTo(BigDecimal.ZERO) > 0) {
                receiptItem.setDiscountedPrice(itemTotal.subtract(maxDiscount));
                appliedDiscounts.add(appliedDiscount);
                totalDiscount = totalDiscount.add(maxDiscount);
            } else {
                receiptItem.setDiscountedPrice(itemTotal);
            }

            totalPrice = totalPrice.add(itemTotal);
            items.add(receiptItem);
        }

        receipt.setItems(items);
        receipt.setAppliedDiscounts(appliedDiscounts);
        receipt.setTotalPrice(totalPrice);
        receipt.setTotalDiscount(totalDiscount);
        receipt.setFinalPrice(totalPrice.subtract(totalDiscount));

        return receipt;
    }
} 