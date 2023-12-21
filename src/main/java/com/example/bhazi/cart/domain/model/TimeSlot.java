package com.example.bhazi.cart.domain.model;

import java.time.LocalTime;

import lombok.Getter;

@Getter
public enum TimeSlot {
	SEVEN_NINE("07:00 - 09:00"),
	NINE_ELEVEN("09:00 - 11:00"),
	ELEVEN_THIRTEEN("11:00 - 13:00"),
	THIRTEEN_FIFTEEN("13:00 - 15:00"),
	FIFTEEN_SEVENTEEN("15:00 - 17:00"),
	SEVENTEEN_NINETEEN("17:00 - 19:00"),
	NINETEEN_TWENTYONE("19:00 - 21:00");
	
	private LocalTime time;
	private String displayTime;
	
	private TimeSlot(String time) {
		this.time = LocalTime.parse(time.substring(0, 5));
		this.displayTime = time;
	}
}
