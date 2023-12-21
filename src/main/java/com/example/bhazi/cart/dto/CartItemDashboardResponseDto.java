package com.example.bhazi.cart.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemDashboardResponseDto {
	private boolean active;
	private int productId;
	private float price;
	private int weight;
	private List<CartItemDetailResponseDto> subProducts;
}
