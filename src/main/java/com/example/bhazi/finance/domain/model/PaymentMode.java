package com.example.bhazi.finance.domain.model;

import lombok.Getter;

@Getter
public enum PaymentMode {
    COD("Cash On Delivery"),
    CASH("Cash"),
    ONLINE("Online"),
    PREPAID("Prepaid"),
    POSTPAID("Postpaid");

    private String description;

    PaymentMode(String description) {
        this.description = description;
    }
}
