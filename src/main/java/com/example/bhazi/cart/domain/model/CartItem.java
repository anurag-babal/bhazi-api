package com.example.bhazi.cart.domain.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartItem {
	private long cartId;
	private int productId;
//	private int subProductId;
//	private int counter;
	private int weight;
	private float price;
	private boolean active = true;
	private List<CartSubProduct> subProducts = new ArrayList<>();
}
