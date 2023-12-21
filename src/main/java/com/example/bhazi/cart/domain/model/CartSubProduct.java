package com.example.bhazi.cart.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartSubProduct {
	private long id;
	private int subProductId;
	private int counter;
	private float price;
	private int weight;
	private boolean active = true;
}
