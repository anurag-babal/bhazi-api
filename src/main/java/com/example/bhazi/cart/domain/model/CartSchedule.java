package com.example.bhazi.cart.domain.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartSchedule {
	ScheduleType scheduleType;
	List<DeliveryTimeSlot> deliveryTimeSlots;
	List<CartItem> itemsNotAvailable;
}
