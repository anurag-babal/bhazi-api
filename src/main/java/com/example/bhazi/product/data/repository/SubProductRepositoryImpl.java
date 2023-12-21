package com.example.bhazi.product.data.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.bhazi.core.exception.EntityNotFoundException;
import com.example.bhazi.product.data.dao.SubProductDao;
import com.example.bhazi.product.data.entity.SubProductEntity;
import com.example.bhazi.product.domain.Product;
import com.example.bhazi.product.domain.SubProduct;
import com.example.bhazi.product.domain.SubProductRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubProductRepositoryImpl implements SubProductRepository {
	private final SubProductDao subProductDao;

	@Override
	public SubProduct saveSubProduct(SubProduct subProduct, Product product) {
		SubProductEntity subProductEntity = mapToEntitySubProduct(subProduct);
		subProductEntity.setProduct(product);
		SubProductEntity savedSubProductEntity = subProductDao.save(subProductEntity);
		return mapToDomainSubProduct(savedSubProductEntity);
	}

	@Override
	public SubProduct updateSubProduct(int id, SubProduct subProduct) {
		SubProductEntity updatedSubProductEntity;
		SubProductEntity subProductEntity = mapToEntitySubProduct(subProduct);
		subProductEntity.setId(id);
		updatedSubProductEntity = subProductDao.save(subProductEntity);
		return mapToDomainSubProduct(updatedSubProductEntity);
	}

	@Override
	public SubProduct getSubProductById(int id) {
		return mapToDomainSubProduct(getSubProductEntityById(id));
	}

	@Override
	public List<SubProduct> getAllSubProductsByProductId(int productId) {
		return subProductDao.findAllByProductId(productId).stream()
				.map(this::mapToDomainSubProduct)
				.collect(Collectors.toList());
	}

	private SubProductEntity getSubProductEntityById(int id) {
		return subProductDao.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("SubProduct not found with id: " + id));
	}

	private SubProduct mapToDomainSubProduct(SubProductEntity entity) {
		SubProduct subProduct = new SubProduct();
		subProduct.setId(entity.getId());
		subProduct.setPrice(entity.getPrice());
		subProduct.setWeight(entity.getWeight());
		subProduct.setProductId(entity.getProduct().getId());
		return subProduct;
	}

	private SubProductEntity mapToEntitySubProduct(SubProduct subProduct) {
		SubProductEntity entity = new SubProductEntity();
		entity.setPrice(subProduct.getPrice());
		entity.setWeight(subProduct.getWeight());
		return entity;
	}
}
