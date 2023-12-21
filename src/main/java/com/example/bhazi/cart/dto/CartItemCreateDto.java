package com.example.bhazi.cart.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartItemCreateDto {
	@NotNull
	private int subProductId;
	@NotNull
	private int counter;
}
