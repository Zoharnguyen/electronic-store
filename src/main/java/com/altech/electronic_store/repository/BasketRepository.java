package com.altech.electronic_store.repository;

import com.altech.electronic_store.model.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import jakarta.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {
    @Lock(LockModeType.OPTIMISTIC)
    Optional<Basket> findWithLockingById(Long id);
}