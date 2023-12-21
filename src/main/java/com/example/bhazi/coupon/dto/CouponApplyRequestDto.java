package com.example.bhazi.coupon.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CouponApplyRequestDto {
    @NotNull
    int profileId;

    @NotNull
    @Size(min = 8, max = 8)
    String couponCode;

    @NotNull
    float discountAmount;
}
