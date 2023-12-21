package com.example.bhazi.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bhazi.order.domain.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
}
