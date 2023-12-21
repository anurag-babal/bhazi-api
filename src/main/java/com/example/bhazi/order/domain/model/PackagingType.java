package com.example.bhazi.order.domain.model;

import lombok.Getter;

@Getter
public enum PackagingType {
	BOX("Box"), BASIC("Basic"), CRATE("Crate"), CARTOON("Cartoon");

	private String description;

	PackagingType(String description) {
		this.description = description;
	}

	public float getCharge() {
		float charge;
		switch (this) {
		case BOX:
			charge = 20;
			break;
		case CRATE:
			charge = 30;
			break;
		default:
			charge = 0;
		}
		return charge;
	}
}
