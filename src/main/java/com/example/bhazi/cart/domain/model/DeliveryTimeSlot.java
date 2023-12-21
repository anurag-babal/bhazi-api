package com.example.bhazi.cart.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeliveryTimeSlot {
	private String timeSlot;
	private float deliveryCharge = 0;
	private boolean active = false;
}
