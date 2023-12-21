package com.example.bhazi.product.data.entity;

import java.io.Serializable;

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

import com.example.bhazi.product.domain.Product;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sub_product")
@Data
@NoArgsConstructor
public class SubProductEntity implements Serializable {
	private static final long serialVersionUID = -2810373330804553500L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@NotNull
	@Column(name = "weight", nullable = false)
	private int weight;
	
	@NotNull
	@Column(name = "price", nullable = false)
	private float price;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;
}
