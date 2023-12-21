package com.example.bhazi.admin.domain;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bhazi.admin.domain.model.Shop;
import com.example.bhazi.core.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShopService {
    private final ShopRepository shopRepository;

    public Shop getById(byte id) {
        Shop shop = shopRepository.findById(id).orElse(null);
        if (shop == null)
            throw new EntityNotFoundException("Shop not found with id = " + id);
        return shop;
    }

    public List<Shop> getAll() {
        return shopRepository.findAll();
    }
}
