package com.example.bhazi.cart.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartCreateDto {
	@NotNull
	private int profileId;
	
	@NotNull
	private int subProductId;
	
	@NotNull
	private int counter;
}
