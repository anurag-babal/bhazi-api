package com.example.bhazi.product.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDetail {
	int id;
	String value;
	byte index;
	ProductDetailType type;
	int productId;
}
