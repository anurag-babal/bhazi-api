package com.example.bhazi.cart.data.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bhazi.cart.data.entity.CartEntity;

public interface CartDao extends JpaRepository<CartEntity, Long>{
	Optional<CartEntity> findByProfileIdAndActive(int profileId, boolean active);
}
