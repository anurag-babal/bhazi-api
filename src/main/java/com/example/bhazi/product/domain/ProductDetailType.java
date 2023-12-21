package com.example.bhazi.product.domain;

import lombok.Getter;

@Getter
public enum ProductDetailType {
	STORAGE("Storage"),
	DESC("Description");
	
	private String description;
	
	private ProductDetailType(String description) {
		this.description = description;
	}
}
