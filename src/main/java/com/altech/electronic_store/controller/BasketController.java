package com.altech.electronic_store.controller;

import com.altech.electronic_store.dto.BasketDTO;
import com.altech.electronic_store.dto.BasketItemDTO;
import com.altech.electronic_store.dto.ReceiptDTO;
import com.altech.electronic_store.service.BasketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/client-api/v1/baskets")
@RequiredArgsConstructor
public class BasketController {
    private final BasketService basketService;

    @PostMapping
    public ResponseEntity<BasketDTO> createBasket() {
        BasketDTO basketDTO = new BasketDTO();
        basketDTO.setId(basketService.createBasket());
        return new ResponseEntity<>
                (basketDTO, HttpStatus.CREATED);
    }

    @PostMapping("/{basketId}/items")
    public ResponseEntity<Void> addItem(
            @PathVariable Long basketId,
            @Valid @RequestBody BasketItemDTO itemDTO) {
        basketService.addItem(basketId, itemDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{basketId}/items/{productId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable Long basketId,
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        basketService.removeItem(basketId, productId, quantity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{basketId}/receipt")
    public ResponseEntity<ReceiptDTO> calculateReceipt(@PathVariable Long basketId) {
        return ResponseEntity.ok(basketService.calculateReceipt(basketId));
    }
} 