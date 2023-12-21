package com.example.bhazi.product.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bhazi.product.data.entity.SubProductEntity;

public interface SubProductDao extends JpaRepository<SubProductEntity, Integer>{
	List<SubProductEntity> findAllByProductId(int productId);
}
