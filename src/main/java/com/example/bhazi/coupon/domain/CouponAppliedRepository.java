package com.example.bhazi.coupon.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bhazi.coupon.domain.model.Coupon;
import com.example.bhazi.coupon.domain.model.CouponApplied;

public interface CouponAppliedRepository extends JpaRepository<CouponApplied, Long> {
    Page<CouponApplied> findAllByProfileIdAndCoupon(
        Integer profileId, 
        Coupon coupon, 
        Pageable pageable
    );
}
