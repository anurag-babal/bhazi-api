package com.example.bhazi.order.domain.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.example.bhazi.admin.domain.model.Shop;
import com.example.bhazi.core.model.AuditModel;
import com.example.bhazi.finance.domain.model.PaymentMode;
import com.example.bhazi.profile.domain.model.Address;
import com.example.bhazi.profile.domain.model.Profile;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity()
@Table(name = "order_history")
@Data
@EqualsAndHashCode(callSuper = false)
public class Order extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private OrderStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode", length = 20, nullable = false)
    private PaymentMode paymentMode;

    @Column(name = "amount", nullable = false, updatable = false)
    private float amount;

    @Column(name = "delivery_charge", nullable = false, updatable = false)
    private float deliveryCharge;

    @Column(name = "packaging_charge", nullable = false, updatable = false)
    private float packagingCharge;

    @Column(name = "coupon_balance", nullable = false)
    private float couponBalance;

    @Column(name = "referral_balance", updatable = false)
    private float referralBalance;

    @Column(name = "wallet_balance", updatable = false)
    private float walletBalance;

    @Column(name = "payable", updatable = false)
    private float payable;

    /* @Column(name = "shop_id", nullable = false)
    private byte shopId; */

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_time_preference", length = 20, nullable = false)
    private DeliveryTimePref deliveryTimePref;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "packaging_type", length = 20, nullable = false)
    private PackagingType packagingType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_type", length = 20, nullable = false)
    private DeliveryType deliveryType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
}
