package com.example.bhazi.product.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubProduct {
	private int id;
	private int weight;
	private float price;
	private int productId;
}
