package com.example.bhazi.cart.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartResponseDto {
	private long id;
	private boolean active;
	
	private String deliveryType;
	private String packagingType;
	private long addressId;
    
    private boolean couponApplied;
    private String couponCode;
    private float couponDiscount;
    
    private boolean walletUsed;
    private float walletBalanceUsed;
    private float referralBalanceUsed;
    private String referralMessage;
    
	private float deliveryCharge;
	private String deliveryMessage;
	private float packagingCharge;
	
	private float totalItemsPrice;
	private float totalDiscount;
	private float payable;
	
	private List<CartItemResponseDto> cartItems;
}
