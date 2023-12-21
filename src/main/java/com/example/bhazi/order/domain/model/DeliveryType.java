package com.example.bhazi.order.domain.model;

import lombok.Getter;

@Getter
public enum DeliveryType {
	HOME_DELIVERY("Home Delivery"), STORE_PICKUP("Store Pickup");

	private String description;

	DeliveryType(String description) {
		this.description = description;
	}

	public float getCharge() {
		float charge;

		switch (this) {
		case STORE_PICKUP:
			charge = 0;
			break;
		case HOME_DELIVERY:
			charge = 20;
			break;
		default:
			charge = 0;
		}
		return charge;
	}
}
