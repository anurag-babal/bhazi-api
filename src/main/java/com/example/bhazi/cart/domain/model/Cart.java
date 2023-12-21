package com.example.bhazi.cart.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.example.bhazi.order.domain.model.DeliveryType;
import com.example.bhazi.order.domain.model.PackagingType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Cart {
	private long id;
	private int profileId;
	
	private boolean active = true;
	private DeliveryType deliveryType = DeliveryType.HOME_DELIVERY;
	private PackagingType packagingType = PackagingType.BASIC;
    
    private boolean couponApplied;
    private String couponCode;
    private float couponDiscount;
    
    private boolean walletUsed;
    private float walletBalanceUsed;
    private float referralBalanceUsed;
    private String referralMessage = "";
    
    private long addressId;
	private float deliveryCharge;
	private String deliveryMessage = "";
	private float packagingCharge;
	
	private float totalItemsPrice;
	private float totalDiscount;
	private float payable;
	
	private Instant updatedAt;
	
	private List<CartItem> cartItems = new ArrayList<>();
}
