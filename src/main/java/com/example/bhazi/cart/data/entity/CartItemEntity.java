package com.example.bhazi.cart.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.example.bhazi.core.model.AuditModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_item")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class CartItemEntity extends AuditModel {
	private static final long serialVersionUID = -2399163643946803155L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@Column(name = "counter", nullable = false)
	private int counter;
	
	@NotNull
	@Column(name = "quantity", nullable = false)
	private int quantity;
	
	@NotNull
	@Column(name = "price", nullable = false)
	private float price;
	
	@NotNull
	@Column(name = "product_id", nullable = false)
	private int productId;
	
	@NotNull
	@Column(name = "sub_product_id", nullable = false)
	private int subProductId;
	
	@NotNull
	@Column(name = "active", nullable = false)
	private boolean active = true;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "cart_id", nullable = false)
	private CartEntity cartEntity;
}
