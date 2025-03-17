package com.altech.electronic_store.service;

import com.altech.electronic_store.dto.BasketItemDTO;
import com.altech.electronic_store.dto.ReceiptDTO;
import com.altech.electronic_store.exception.ResourceNotFoundException;
import com.altech.electronic_store.model.*;
import com.altech.electronic_store.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private DiscountRepository discountRepository;

    @InjectMocks
    private BasketService basketService;

    private Basket basket;
    private Product product;
    private BasketItemDTO itemDTO;

    @BeforeEach
    void setUp() {
        basket = new Basket();
        basket.setId(1L);
        basket.setItems(new ArrayList<>());

        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(new BigDecimal("999.99"));
        product.setStockQuantity(10);

        itemDTO = new BasketItemDTO();
        itemDTO.setProductId(1L);
        itemDTO.setQuantity(2);
    }

    @Test
    void createBasket_Success() {
        when(basketRepository.save(any(Basket.class))).thenReturn(basket);

        Long result = basketService.createBasket();

        assertNotNull(result);
        assertEquals(basket.getId(), result);
        verify(basketRepository, times(1)).save(any(Basket.class));
    }

    @Test
    void addItem_Success() {
        when(basketRepository.findWithLockingById(1L)).thenReturn(Optional.of(basket));
        when(productRepository.findWithLockingById(1L)).thenReturn(Optional.of(product));
        when(basketRepository.save(any(Basket.class))).thenReturn(basket);

        assertDoesNotThrow(() -> basketService.addItem(1L, itemDTO));

        verify(basketRepository, times(1)).save(any(Basket.class));
        assertEquals(8, product.getStockQuantity());
    }

    @Test
    void addItem_InsufficientStock() {
        product.setStockQuantity(1);
        when(basketRepository.findWithLockingById(1L)).thenReturn(Optional.of(basket));
        when(productRepository.findWithLockingById(1L)).thenReturn(Optional.of(product));

        assertThrows(IllegalStateException.class, () -> basketService.addItem(1L, itemDTO));
    }

    @Test
    void calculateReceipt_Success() {
        BasketItem basketItem = new BasketItem();
        basketItem.setProduct(product);
        basketItem.setQuantity(2);
        basket.getItems().add(basketItem);

        Discount discount = new Discount();
        discount.setProduct(product);
        discount.setMinimumQuantity(2);
        discount.setDiscountPercentage(new BigDecimal("10"));
        discount.setDescription("10% off on 2 or more");

        when(basketRepository.findById(1L)).thenReturn(Optional.of(basket));
        when(discountRepository.findByProductId(1L)).thenReturn(List.of(discount));

        ReceiptDTO receipt = basketService.calculateReceipt(1L);

        assertNotNull(receipt);
        assertEquals(1, receipt.getItems().size());
        assertTrue(receipt.getTotalDiscount().compareTo(BigDecimal.ZERO) > 0);
    }
} 