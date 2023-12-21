package com.example.bhazi.cart.domain.model;

import lombok.Getter;

@Getter
public enum ScheduleType {
	SAME_DAY("Same Day"),
	NEXT_DAY("Next Day"),
	INSTANT("Instant");
	
	private String displayType;
	
	private ScheduleType(String type) {
		this.displayType = type;
	}
}
