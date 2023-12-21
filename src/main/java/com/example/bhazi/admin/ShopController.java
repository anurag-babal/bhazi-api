package com.example.bhazi.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bhazi.admin.domain.ShopService;
import com.example.bhazi.admin.dto.ShopResponseDto;
import com.example.bhazi.core.dto.ResponseDto;
import com.example.bhazi.util.ResponseBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/shop")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService shopService;
    private final ShopMapper shopMapper;

    @GetMapping("/{id}")
    public ResponseDto getById(@PathVariable(name = "id") byte id) {
        ShopResponseDto shop = shopMapper.mapToDto(
            shopService.getById(id)
        );

        return ResponseBuilder.createResponse(shop);
    }

    @GetMapping("")
    public ResponseDto findAll() {
        Map<String, ShopResponseDto> shopMap = new HashMap<>();
        shopService.getAll().forEach(shop -> {
            shopMap.put(shop.getTag().toUpperCase(), shopMapper.mapToDto(shop));
        });

        return ResponseBuilder.createListResponse(shopMap.size(), shopMap);
    }
}
