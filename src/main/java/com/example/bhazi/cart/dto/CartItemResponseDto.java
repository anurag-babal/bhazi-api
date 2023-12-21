package com.example.bhazi.cart.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemResponseDto {
	private int productId;
	private boolean active;
	private String name;
	private String nameHindi;
	private String imageUrl;
	private float price;
	private int weight;
	private List<CartItemDetailResponseDto> subProducts;
}
