package com.example.bhazi.product.data.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bhazi.product.data.entity.ProductDetailEntity;
import com.example.bhazi.product.domain.ProductDetailType;

public interface ProductDetailDao extends JpaRepository<ProductDetailEntity, Integer> {

	Page<ProductDetailEntity> findAllByProductIdAndType(int productId, ProductDetailType type, Pageable pageable);

}
