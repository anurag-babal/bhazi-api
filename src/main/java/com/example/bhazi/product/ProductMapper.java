package com.example.bhazi.product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.bhazi.product.domain.Product;
import com.example.bhazi.product.domain.ProductService;
import com.example.bhazi.product.domain.SubProduct;
import com.example.bhazi.product.dto.ProductCreateDto;
import com.example.bhazi.product.dto.ProductResponseDto;
import com.example.bhazi.product.dto.ProductUpdateDto;
import com.example.bhazi.product.dto.SubProductDto;

@Component
public class ProductMapper {
	@Autowired
	private ProductService productService;
    
    public Product mapToDomain(ProductCreateDto productCreateDto) {
        Product product = new Product();
        product.setName(productCreateDto.getName());
        product.setBasePrice(productCreateDto.getPrice());
        product.setDescription(productCreateDto.getDescription());
        product.setType(productCreateDto.getType());
        product.setSubType(productCreateDto.getSubType());
        product.setBaseQuantity(productCreateDto.getBaseQuantity());
        product.setSoldByPiece(productCreateDto.getSoldByPiece());
        product.setNumberOfPieces(productCreateDto.getNumberOfPieces());

        product.setNameHindi(productCreateDto.getNameHindi());
        product.setImageUrl(productCreateDto.getImageUrl());
        product.setDescriptionStorage(productCreateDto.getDescriptionStorage());
        product.setOutOfStock(productCreateDto.isOutOfStock());
        product.setPrime(productCreateDto.isPrime());
        return product;
    }

    public Product mapToDomain(ProductUpdateDto productUpdateDto, Product product) {
        product.setName(productUpdateDto.getName());
        product.setDescription(productUpdateDto.getDescription());
        product.setType(productUpdateDto.getType());
        product.setSubType(productUpdateDto.getSubType());
        product.setBaseQuantity(productUpdateDto.getBaseQuantity());
        product.setSoldByPiece(productUpdateDto.getSoldByPiece());
        product.setNumberOfPieces(productUpdateDto.getNumberOfPieces());
        return product;
    }

    public ProductResponseDto mapToDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .nameHindi(product.getNameHindi())
                .imageUrl(product.getImageUrl())
                .type(product.getType())
                .subType(product.getSubType())
                .price(product.getSellingPrice())
                .primeProduct(product.isPrime())
                .outOfStock(product.isOutOfStock())
                .soldByPiece(product.getSoldByPiece())
                .description(product.getDescription())
                .descriptionStorage(product.getDescriptionStorage())
                .baseQuantity(product.getBaseQuantity())
                .numberOfPieces(product.getNumberOfPieces())
                .subProducts(getSubProducts(product.getId()))
                .build();
    }

    public List<ProductResponseDto> mapTDtos(List<Product> products) {
        return products.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    public SubProductDto mapToResponseSubProductDto(SubProduct subProduct) {
    	return SubProductDto.builder()
    			.id(subProduct.getId())
    			.price(subProduct.getPrice())
    			.weight(subProduct.getWeight())
    			.build();
    }
    
    public List<SubProductDto> mapToResponseSubProductDtos(List<SubProduct> subProducts) {
    	return subProducts.stream()
    			.map(this::mapToResponseSubProductDto)
    			.collect(Collectors.toList());
    }

    public List<SubProductDto> getSubProducts(int productId) {
		return mapToResponseSubProductDtos(productService.getAllSubProductsByProductId(productId));
    }
}
