package com.example.bhazi.cart.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemDetailResponseDto {
	private long id;
	private int subProductId;
	private int counter;
	private float price;
	private int weight;
	private boolean active;
}
