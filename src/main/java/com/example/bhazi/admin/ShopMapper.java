package com.example.bhazi.admin;

import org.springframework.stereotype.Component;

import com.example.bhazi.admin.domain.model.Shop;
import com.example.bhazi.admin.dto.ShopResponseDto;

// @Mapper(componentModel = "spring")
@Component
public class ShopMapper {
    public ShopResponseDto mapToDto(Shop shop) {
        return ShopResponseDto.builder()
                .id(shop.getId())
                .tag(shop.getTag())
                .address(shop.getAddress())
                .lattitude(shop.getLattitude())
                .longitude(shop.getLongitude())
                .build();
    }

    public ShopResponseDto mapToBasicDto(Shop shop) {
        return ShopResponseDto.builder()
                .id(shop.getId())
                .tag(shop.getTag())
                .address(shop.getAddress())
                .build();
    }
}
