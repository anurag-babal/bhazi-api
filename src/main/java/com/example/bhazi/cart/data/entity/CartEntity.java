package com.example.bhazi.cart.data.entity;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.example.bhazi.core.model.AuditModel;
import com.example.bhazi.order.domain.model.DeliveryType;
import com.example.bhazi.order.domain.model.PackagingType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class CartEntity extends AuditModel {
	private static final long serialVersionUID = 8869561891963998891L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@Column(name = "active", nullable = false)
	private boolean active = true;
	
	@NotNull
    @Column(name = "addressId", nullable = false)
	private long addressId;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "packaging_type", length = 20, nullable = false)
    private PackagingType packagingType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_type", length = 20, nullable = false)
    private DeliveryType deliveryType;
	
	@NotNull
	@Column(name = "coupon_applied")
	private boolean couponApplied = false;
	
	@Column(name = "coupon_code")
	private String couponCode;
	
	@NotNull
	@Column(name = "coupon_discount", nullable = false)
	private float couponDiscount = 0;
	
	@NotNull
	@Column(name = "wallet_used", nullable = false)
	private boolean walletUsed = false;
	
	@NotNull
	@Column(name = "wallet_balance_used", nullable = false)
	private float walletBalanceUsed = 0;
	
	@NotNull
	@Column(name = "referral_balance_used", nullable = false)
	private float referralBalanceUsed = 0;
	
	@NotNull
	@Column(name = "packaging_charge", nullable = false)
	private float packagingCharge = 0;
	
	@NotNull
	@Column(name = "delivery_charge", nullable = false)
	private float deliveryCharge = 0;
	
	@NotNull
	@Column(name = "total_amount", nullable = false)
	private float totalAmount = 0;
	
	@NotNull
	@Column(name = "total_discount", nullable = false)
	private float totalDiscount = 0;
	
	@NotNull
	@Column(name = "payable", nullable = false)
	private float payable = 0;
	
	@NotNull
	@Column(name = "profile_id", nullable = false)
	private int profileId;
	
	@OneToMany(mappedBy = "cartEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<CartItemEntity> cartItems;
}
