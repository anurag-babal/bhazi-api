package com.example.bhazi.cart.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartUpdateDto {
	private String deliveryType;
	private String packagingType;
	private String deliveryTimePref;
	
	private boolean couponApplied;
	private boolean walletUsed;
}
