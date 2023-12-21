package com.example.bhazi.cart.util;

import java.util.stream.Stream;

import com.example.bhazi.order.domain.model.DeliveryTimePref;

public class GetDeliveryTimePref {
	public DeliveryTimePref invoke(String time) {
		return Stream.of(DeliveryTimePref.values())
                .filter(deliveryTime -> deliveryTime.getDescription().equals(time))
                .findFirst()
                .orElse(DeliveryTimePref.ALL_DAY);
	}
}
