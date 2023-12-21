package com.example.bhazi.product.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bhazi.admin.domain.ChargeService;
import com.example.bhazi.admin.domain.model.Charge;
import com.example.bhazi.core.exception.EntityNotFoundException;
import com.example.bhazi.product.data.dao.ProductDao;
import com.example.bhazi.product.data.repository.SubProductRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductDao productDao;
	private final ProductRepository productRepository;
	private final SubProductRepository subProductRepository;
	private final ChargeService chargeService;

	@Transactional
	public Product save(Product product) {
		Product savedProduct;
		updateSellingPrice(product);
		savedProduct = productDao.save(product);
		generateSubProductsAndSave(savedProduct);
		return savedProduct;
	}

	@Transactional
	public void refreshProducts(List<Product> products) {
		products.forEach(this::generateSubProductsAndSave);
	}
	
	private void generateSubProductsAndSave(Product product) {
		int[] weightsFor100Gm = { 100, 250, 500 };
		int[] weightsFor250Gm = { 250, 500, 1000 };
		int[] weightsFor500Gm = { 500, 1000, 2000 };
		int[] weightsFor1Kg = { 1000, 2000, 3000 };
		int[] weightsFor2Kg = { 2000, 4000, 6000 };
		float[] levels = { 1.0f, 0.9f, 0.85f };
		switch (product.getBaseQuantity()) {
		case "100 Gm":
			abc(weightsFor100Gm, levels, product);
			break;
		case "250 Gm":
			abc(weightsFor250Gm, levels, product);
			break;
		case "500 Gm":
			abc(weightsFor500Gm, levels, product);
			break;
		case "1 kg":
			abc(weightsFor1Kg, levels, product);
			break;
		case "2 kg":
			abc(weightsFor2Kg, levels, product);
			break;
		default:
			break;
		}
	}

	private void abc(int[] weights, float[] levels, Product product) {
		SubProduct subProduct = new SubProduct();
		List<SubProduct> subProducts = getAllSubProductsByProductId(product.getId());

		subProduct.setProductId(product.getId());
		int baseQuantity = weights[0];
		int productPrice = product.getSellingPrice();
		for (int i = 0; i < weights.length; i++) {
			int weight = weights[i];
			int price = (int) Math.floor((productPrice * (weight / baseQuantity)) * levels[i]);
			subProduct.setWeight(weights[i]);
			subProduct.setPrice(price);
			Optional<SubProduct> oldSubProduct = subProducts.stream()
					.filter(item -> item.getWeight() == weight)
					.findFirst();
			if (oldSubProduct.isPresent())
				updateSubProduct(oldSubProduct.get().getId(), subProduct);
			else
				saveSubProduct(subProduct, product);
		}
	}

	private SubProduct saveSubProduct(SubProduct subProduct, Product product) {
		return subProductRepository.saveSubProduct(subProduct, product);
	}

	private SubProduct updateSubProduct(int subProductId, SubProduct subProduct) {
		return subProductRepository.updateSubProduct(subProductId, subProduct);
	}
	
	@Transactional
	public Product update(int id, Product product) {
		Product updatedProduct;
		updatedProduct = productDao.save(product);
		return updatedProduct;
	}

	@Transactional
	public Product updateStock(int id, boolean outOfStock) {
		Product product, updatedProduct;
		product = getById(id);
		product.setOutOfStock(outOfStock);
		updatedProduct = productDao.save(product);
		return updatedProduct;
	}

	@Transactional
	public Product updatePrimeStatus(int id, boolean prime) {
		Product product = getById(id);
		product.setPrime(prime);
		return productDao.save(product);
	}

	@Transactional
	public Product updatePrice(int id, int price) {
		Product product;
		Product updatedProduct;
		product = getById(id);
		product.setBasePrice(price);
		updateSellingPrice(product);
		updatedProduct = productDao.save(product);
		generateSubProductsAndSave(updatedProduct);
		return updatedProduct;
	}

	private void updateSellingPrice(Product product) {
		product.setSellingPrice(getCharge(product.getBasePrice()));
	}

	private int getCharge(int price) {
		int totalCharge = price;
		Charge charge = chargeService.getLatest();
		totalCharge += price * (charge.getApi() / 100);
		totalCharge += price * (charge.getPlatform() / 100);
		totalCharge += price * (charge.getHosting() / 100);
		totalCharge += price * (charge.getProfit() / 100);
		totalCharge += price * (charge.getSlippage() / 100);
		totalCharge += Math.round(charge.getDelivery() + charge.getHandling() + charge.getNetwork()
				+ charge.getOperation() + charge.getTransport());

		return totalCharge;
	}

	public boolean delete(int id) {
		Product product = getById(id);
		productDao.delete(product);
		return true;
	}

	public Product getById(int id) {
		Product product = productDao.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Product not found with id = " + id));
		return product;
	}

	public Product getByName(String name) {
		Product product = productDao.findByName(name)
				.orElseThrow(() -> new EntityNotFoundException("Product not found with name = " + name));
		return product;
	}

	public Page<Product> getAllAvailable(Pageable pageable) {
		Page<Product> products = productDao.findByOutOfStock(false, pageable);
		return products;
	}

	public Page<Product> getAll(Pageable pageable) {
		Page<Product> products = productDao.findAll(pageable);
		return products;
	}

	public Page<Product> getAllPrime(Pageable pageable) {
		Page<Product> products = productDao.findAllByPrimeAndOutOfStock(true, false, pageable);
		return products;
	}

	public SubProduct getSubProductById(int id) {
		return subProductRepository.getSubProductById(id);
	}

	public List<SubProduct> getAllSubProductsByProductId(int productId) {
		return subProductRepository.getAllSubProductsByProductId(productId);
	}

	public Page<Product> getAllAvailableByType(String type, Pageable pageable) {
		return productDao.findByOutOfStockAndType(false, type, pageable);
	}

	public List<ProductDetail> getProductDetailByProductIdAndType(int id, ProductDetailType type) {
		return productRepository.getProductDetailByProductIdAndType(id, type);		
	}
}
