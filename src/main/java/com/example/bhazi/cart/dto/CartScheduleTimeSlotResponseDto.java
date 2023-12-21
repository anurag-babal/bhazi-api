package com.example.bhazi.cart.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartScheduleTimeSlotResponseDto {
	String timeSlot;
	float deliveryCharge;
	boolean active;
}
