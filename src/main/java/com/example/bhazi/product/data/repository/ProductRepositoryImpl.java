package com.example.bhazi.product.data.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.example.bhazi.product.data.dao.ProductDetailDao;
import com.example.bhazi.product.data.entity.ProductDetailEntity;
import com.example.bhazi.product.domain.Product;
import com.example.bhazi.product.domain.ProductDetail;
import com.example.bhazi.product.domain.ProductDetailType;
import com.example.bhazi.product.domain.ProductRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

	private final ProductDetailDao productDetailDao;

	@Override
	public Product saveProduct(Product product) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product updateProduct(int productId, Product product) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product getProductById(int productId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductDetail saveProductDetail(ProductDetail productDetail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductDetail updateProductDetail(int productDetailId, ProductDetail productDetail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductDetail> getProductDetailByProductId(int productId, ProductDetail productDetail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductDetail> getProductDetailByProductIdAndType(int productId, ProductDetailType type) {
		Pageable pageable = PageRequest.of(0, 20, Sort.by("index"));
		List<ProductDetailEntity> entities = productDetailDao.findAllByProductIdAndType(productId, type, pageable)
				.getContent();
		return mapToModelProductDetails(entities);
	}

	private ProductDetail mapToModelProductDetail(ProductDetailEntity entity) {
		return ProductDetail.builder().index(entity.getIndex()).type(entity.getType()).index(entity.getIndex())
				.value(entity.getValue()).productId(entity.getProductId()).build();
	}

	private List<ProductDetail> mapToModelProductDetails(List<ProductDetailEntity> entities) {
		return entities.stream().map(this::mapToModelProductDetail).collect(Collectors.toList());
	}
}
