package com.example.bhazi.coupon.domain.model;

import lombok.Getter;

@Getter
public enum CouponType {
    SINGLE("Single"),
    MULTIPLE("Multiple");

    private String description;

    private CouponType(String description) {
        this.description = description;
    }
}
