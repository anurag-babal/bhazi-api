package com.example.bhazi.cart.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.bhazi.cart.domain.model.Cart;
import com.example.bhazi.cart.domain.model.CartItem;
import com.example.bhazi.cart.domain.model.CartSubProduct;

public interface CartRepository {
	Cart saveCart(Cart cart);
	Cart updateCart(long id, Cart cart);
	Cart getCartById(long id);
	Optional<Cart> getCartByProfileId(int profileId);
	Cart getCartByCartSubProductId(long cartSubProductId);
	
	CartItem saveCartSubProduct(long cartId, int productId, CartSubProduct cartSubProduct);
	CartItem updateCartSubProduct(long cartSubProductId, long cartId, int productId,
			CartSubProduct cartSubProduct);
	CartItem getCartItemById(long cartItemId);
	Page<CartItem> getCartItemsByCartId(long cartId, Pageable pageable);
	
	CartSubProduct getCartSubProductById(long cartSubProductId);
	CartSubProduct getCartSubProductBySubProductId(long cartId, int subProductId);
}
