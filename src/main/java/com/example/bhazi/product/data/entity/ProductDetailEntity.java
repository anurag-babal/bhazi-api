package com.example.bhazi.product.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.example.bhazi.product.domain.ProductDetailType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_detail")
@Data
@NoArgsConstructor
public class ProductDetailEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@NotNull
	@Column(name = "value", nullable = false, length = 255)
	private String value;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false, length = 20)
	private ProductDetailType type;
	
	@NotNull
	@Column(name = "index", nullable = false)
	private byte index;
	
	@NotNull
	@Column(name = "product_id", nullable = false)
	private int productId;
}
