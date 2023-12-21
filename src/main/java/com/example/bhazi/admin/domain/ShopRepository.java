package com.example.bhazi.admin.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bhazi.admin.domain.model.Shop;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Byte> {
    // public List<Shop> findAllByOrderByIdAsc();
}
