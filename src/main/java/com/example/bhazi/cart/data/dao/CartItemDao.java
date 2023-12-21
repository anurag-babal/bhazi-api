package com.example.bhazi.cart.data.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bhazi.cart.data.entity.CartItemEntity;

public interface CartItemDao extends JpaRepository<CartItemEntity, Long>{
	Page<CartItemEntity> findAllByCartEntityId(long cartId, Pageable pageable);
	List<CartItemEntity> findAllByProductIdAndActive(int productId, boolean active);
	List<CartItemEntity> findAllByCartEntityIdAndProductId(long cartEntityId, int productId);
	CartItemEntity findByCartEntityIdAndSubProductId(long cartEntityId, int subProductId);
}
