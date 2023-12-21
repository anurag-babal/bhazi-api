package com.example.bhazi.cart.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartDashboardResponseDto {
	private long id;
	private boolean active;
	private float totalItemsPrice;
	private CartItemDashboardResponseDto cartItem; 
}
