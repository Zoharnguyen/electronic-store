package com.altech.electronic_store.service;

import com.altech.electronic_store.dto.BasketItemDTO;
import com.altech.electronic_store.model.Product;
import com.altech.electronic_store.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class ConcurrentBasketOperationsTest {

    @Autowired
    private BasketService basketService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testConcurrentBasketOperations() throws InterruptedException {
        // Setup
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(new BigDecimal("100.00"));
        product.setStockQuantity(100);
        Product savedProduct = productRepository.save(product);

        Long basketId = basketService.createBasket();

        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        // Act
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    BasketItemDTO itemDTO = new BasketItemDTO();
                    itemDTO.setProductId(savedProduct.getId());
                    itemDTO.setQuantity(1);
                    basketService.addItem(basketId, itemDTO);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();

        // Assert
        Product updatedProduct = productRepository.findById(savedProduct.getId()).orElseThrow();
        assertEquals(99, updatedProduct.getStockQuantity());
    }
}