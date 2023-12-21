package com.example.bhazi.cart.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartScheduleDto {
	String type;
	List<CartScheduleTimeSlotResponseDto> timeSlots;
	List<CartItemResponseDto> itemsNotAvailable;
}
