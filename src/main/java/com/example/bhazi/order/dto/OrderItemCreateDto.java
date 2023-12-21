package com.example.bhazi.order.dto;

import lombok.Data;

@Data
public class OrderItemCreateDto {
    private int productId;
    private String productName;
    private int quantity;
    private int price;
}
