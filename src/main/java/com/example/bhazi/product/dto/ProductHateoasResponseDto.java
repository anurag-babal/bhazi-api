package com.example.bhazi.product.dto;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "products", itemRelation = "product")
public class ProductHateoasResponseDto extends RepresentationModel<ProductHateoasResponseDto> {
    int id;
    String name;
    String nameHindi;
    String imageUrl;
    String type;
    String displayWeight;
    String subType;
    String soldByPiece;
    byte numberOfPieces;
    String description;
    List<SubProductDto> subProducts;
}

