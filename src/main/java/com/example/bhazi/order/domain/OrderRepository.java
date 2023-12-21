package com.example.bhazi.order.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bhazi.order.domain.model.Order;
import com.example.bhazi.order.domain.model.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderDateAndStatus(LocalDate date, OrderStatus status);
    List<Order> findAllByOrderDate(LocalDate date);

    Page<Order> findByProfileId(Integer profileId, Pageable pageable);
	Optional<Order> findTopByProfileIdOrderByIdDesc(int profileId);
}
