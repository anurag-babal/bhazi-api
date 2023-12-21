package com.example.bhazi.product.domain;

import java.util.List;

public interface SubProductRepository {
	SubProduct saveSubProduct(SubProduct subProduct, Product product);
	SubProduct updateSubProduct(int subProductId, SubProduct subProduct);
	SubProduct getSubProductById(int id);
	List<SubProduct> getAllSubProductsByProductId(int productId);
}
