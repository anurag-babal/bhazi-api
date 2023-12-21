package com.example.bhazi.coupon.domain.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.bhazi.core.model.AuditModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coupon")
@Data()
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Coupon extends AuditModel {
    @Id
    @Column(name = "coupon_code", length = 8)
    private String couponCode;

    @NotNull
    @Column(name = "discount_percent", nullable = false)
    private int discountPercent;

    @NotNull
    @Column(name = "discount_limit", nullable = false)
    private int discountLimit = 0;

    @NotNull
    @Column(name = "min_amount", nullable = false)
    private int minAmount = 0;

    @Size(max = 255)
    @NotNull
    @Column(name = "description", length = 255, nullable = false)
    private String description;

    @NotNull
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20, nullable = false)
    private CouponType couponType;

    @Column(name = "valid", nullable = false)
    private boolean valid = true;
}
