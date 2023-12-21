package com.example.bhazi.order.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponseDto {
    long id;
    String productName;
    int quantity, price;

    // TODO: Deprecated
    int productId;
}
