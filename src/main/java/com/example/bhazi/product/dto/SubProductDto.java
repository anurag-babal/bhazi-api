package com.example.bhazi.product.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubProductDto {
	int id;
    int weight;
    float price;
}
