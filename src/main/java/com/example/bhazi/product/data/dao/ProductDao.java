package com.example.bhazi.product.data.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bhazi.product.domain.Product;

public interface ProductDao extends JpaRepository<Product, Integer>{
    public Optional<Product> findByName(String name);
    public Page<Product> findByOutOfStock(Boolean outOfStock, Pageable pageable);
    public Page<Product> findAllByPrimeAndOutOfStock(boolean prime, boolean outOfStock, Pageable pageable);
    public Page<Product> findByOutOfStockAndType(Boolean outOfStock, String type, Pageable pageable);
}
