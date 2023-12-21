package com.example.bhazi.order.dto.admin;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalOrderQuantityResponseDto {
    String source;
    List<ItemWiseQuantityResponseDto> items;
    int totalOrders;
    float totalWeight;
}
