package com.example.bhazi.product.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponseDto {
    int id;
    String name;
    String nameHindi;
    String imageUrl;
    String baseQuantity;
    String soldByPiece;
    String type;
    String subType;
    byte numberOfPieces;
    boolean primeProduct;
    boolean outOfStock;
    List<String> desc;
    List<String> storageInstruction;
    List<SubProductDto> subProducts;

    // Deprecated
    int price;
    String description;
    String descriptionStorage;
}
