package com.example.bhazi.cart.domain.service;

import com.example.bhazi.cart.domain.model.Cart;
import com.example.bhazi.cart.domain.model.CartItem;
import com.example.bhazi.cart.domain.model.CartSchedule;
import com.example.bhazi.cart.domain.model.CartSubProduct;

public interface CartService {
	Cart updateCart(long cartId, Cart cart);
	Cart updateDeliveryType(long cartId, String string);
	Cart updatePackagingType(long cartId, String string);
	Cart updateCouponCode(long cartId, String string);
	Cart updateWalletUsed(long cartId, boolean walletUsed);
	Cart updateCouponApplied(long cartId, boolean couponApplied);
	Cart updateAddressId(long cartId, long addressId);
	
	Cart getCartById(long cartId);
	Cart getCartByProfileId(int profileId);
	
	CartItem createCartAndAddCartItem(int profileId, CartSubProduct cartSubProduct);
	
	CartSchedule getCartSchedule(long cartId, String type);
	CartItem updateCartItem(long cartId, int subProductId, int counter);
}
