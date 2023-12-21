package com.example.bhazi.order.domain.model;

import lombok.Getter;

@Getter
public enum OrderStatus {
    ORDERED("Ordered"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled"),
    ON_THE_WAY("Out for delivery"),
    OUT_FOR_DELIVERY("Out for delivery");

    private String description;

    OrderStatus(String description) {
        this.description = description;
    }
}
