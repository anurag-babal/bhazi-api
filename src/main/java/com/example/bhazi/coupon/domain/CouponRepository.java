package com.example.bhazi.coupon.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bhazi.coupon.domain.model.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, String> {
    Page<Coupon> findAllByValid(Boolean valid, Pageable pageable);
}
