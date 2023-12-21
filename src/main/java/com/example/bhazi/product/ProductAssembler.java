package com.example.bhazi.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.example.bhazi.product.domain.Product;
import com.example.bhazi.product.dto.ProductHateoasResponseDto;

@Component
public class ProductAssembler extends 
    RepresentationModelAssemblerSupport<Product, ProductHateoasResponseDto> {
    @Autowired
    private ProductMapper productMapper;

    public ProductAssembler() {
        super(ProductContoller.class, ProductHateoasResponseDto.class);
    }

    @Override
    public ProductHateoasResponseDto toModel(Product entity) {
        ProductHateoasResponseDto response = instantiateModel(entity);
        response.add(WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder
                .methodOn(ProductContoller.class).getById(entity.getId()))
                .withSelfRel()
        );
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setNameHindi(entity.getNameHindi());
        response.setImageUrl(entity.getImageUrl());
        response.setType(entity.getType());
        response.setDisplayWeight(entity.getBaseQuantity());
        response.setSubType(entity.getSubType());
        response.setSoldByPiece(entity.getSoldByPiece());
        response.setNumberOfPieces(entity.getNumberOfPieces());
        response.setDescription(entity.getDescription());
        response.setSubProducts(productMapper.getSubProducts(entity.getId()));
        return response;
    }
}
