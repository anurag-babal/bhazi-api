package com.example.bhazi.order.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemWiseQuantityResponseDto {
    int productId;
    String productName;
    float quantity;
}
