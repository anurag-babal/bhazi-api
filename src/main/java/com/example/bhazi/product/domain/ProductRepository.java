package com.example.bhazi.product.domain;

import java.util.List;

public interface ProductRepository {
	Product saveProduct(Product product);

	Product updateProduct(int productId, Product product);

	Product getProductById(int productId);

	ProductDetail saveProductDetail(ProductDetail productDetail);

	ProductDetail updateProductDetail(int productDetailId, ProductDetail productDetail);

	List<ProductDetail> getProductDetailByProductId(int productId, ProductDetail productDetail);

	List<ProductDetail> getProductDetailByProductIdAndType(int productId, ProductDetailType type);
}
