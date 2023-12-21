package com.example.bhazi.order.domain.model;

import java.util.stream.Stream;

import lombok.Getter;

@Getter
public enum DeliveryTimePref {
    MORNING("BEFORE 9:00 AM"),
    EVENING("AFTER 06:00 PM"),
    OFFICE("IN OFFICE"),
    DAY("09:00 AM - 04:00 PM"),
    BEFORE_EVENING("04:00 PM - 06:00 PM"),
    ALL_DAY("ALL DAY");

    private String description;

    DeliveryTimePref(String description) {
        this.description = description;
    }
    
    public DeliveryTimePref getDeliveryTime(String time) {
    	return Stream.of(DeliveryTimePref.values())
                .filter(deliveryTime -> deliveryTime.getDescription().equals(time))
                .findFirst()
                .orElse(DeliveryTimePref.ALL_DAY);
    }
}
