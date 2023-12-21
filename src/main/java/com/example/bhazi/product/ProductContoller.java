package com.example.bhazi.product;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bhazi.core.dto.ResponseDto;
import com.example.bhazi.product.domain.Product;
import com.example.bhazi.product.domain.ProductDetail;
import com.example.bhazi.product.domain.ProductDetailType;
import com.example.bhazi.product.domain.ProductService;
import com.example.bhazi.product.dto.ProductCreateDto;
import com.example.bhazi.product.dto.ProductHateoasResponseDto;
import com.example.bhazi.product.dto.ProductPriceUpdateDto;
import com.example.bhazi.product.dto.ProductResponseDto;
import com.example.bhazi.product.dto.ProductStockUpdateDto;
import com.example.bhazi.product.dto.ProductUpdateDto;
import com.example.bhazi.util.ResponseBuilder;

import lombok.RequiredArgsConstructor;

@RestController()
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductContoller {

	private final ProductService productService;
	private final ProductMapper productMapper;
	private final PagedResourcesAssembler<Product> pagedResourcesAssembler;
	private final ProductAssembler productAssembler;

	@PostMapping("/products")
	public ResponseDto saveProduct(@Valid @RequestBody ProductCreateDto productCreateDto) {
		Product savedProduct = productService.save(productMapper.mapToDomain(productCreateDto));
		return ResponseBuilder.createResponse(productMapper.mapToDto(savedProduct));
	}

	@PutMapping("/products/{id}")
	public ResponseDto updateProduct(@PathVariable(value = "id") int id,
			@Valid @RequestBody ProductUpdateDto productUpdateDto) {
		Product product = productService.getById(id);
		Product updatedProduct = productService.save(productMapper.mapToDomain(productUpdateDto, product));
		return ResponseBuilder.createResponse(productMapper.mapToDto(updatedProduct));
	}

	@PutMapping("/products/price/{id}")
	public ResponseDto updateProductPrice(@PathVariable(value = "id") int id,
			@Valid @RequestBody ProductPriceUpdateDto productPriceUpdateDto) {
		Product updatedProduct = productService.updatePrice(id, productPriceUpdateDto.getPrice());
		return ResponseBuilder.createResponse(productMapper.mapToDto(updatedProduct));
	}

	@PutMapping("/products/stock/{id}")
	public ResponseDto updateProductStock(@PathVariable(value = "id") int id,
			@Valid @RequestBody ProductStockUpdateDto productStockUpdateDto) {
		Product updatedProduct = productService.updateStock(id, productStockUpdateDto.isOutOfStock());
		return ResponseBuilder.createResponse(productMapper.mapToDto(updatedProduct));
	}

	@DeleteMapping("/products/{id}")
	public ResponseDto deleteProduct(@PathVariable(value = "id") int id) {
		return ResponseBuilder.createDeleteResponse(productService.delete(id));
	}

	@GetMapping("/products")
	public ResponseDto getAllAvailable() {
		Pageable pageable = PageRequest.of(0, 200);
		List<Product> products = productService.getAllAvailable(pageable).getContent();
		List<ProductResponseDto> responseDtos = productMapper.mapTDtos(products);
		return ResponseBuilder.createListResponse(responseDtos.size(), responseDtos);
	}
	
	@GetMapping("/products/{id}")
	public ResponseDto getById(@PathVariable(value = "id") int id) {
		Product product = productService.getById(id);
		List<ProductDetail> desc = productService.getProductDetailByProductIdAndType(id, ProductDetailType.DESC);
		List<ProductDetail> storage = productService.getProductDetailByProductIdAndType(id, ProductDetailType.STORAGE);
		ProductResponseDto responseDto = productMapper.mapToDto(product);
		responseDto.setDesc(desc.stream().map(ProductDetail::getValue).collect(Collectors.toList()));
		responseDto.setStorageInstruction(storage.stream().map(ProductDetail::getValue).collect(Collectors.toList()));
		return ResponseBuilder.createResponse(responseDto);
	}
	
	
	@PutMapping("/v1/products/{id}")
	public ResponseDto updateProductPrimeStatus(@PathVariable(value = "id") int id,
			@RequestParam(name = "prime") boolean prime) {
		Product updatedProduct = productService.updatePrimeStatus(id, prime);
		return ResponseBuilder.createResponse(productMapper.mapToDto(updatedProduct));
	}

	@PutMapping("/v1/products")
	public ResponseDto refreshProducts(@RequestParam(name = "refresh", defaultValue = "false") boolean refresh) {
		Pageable pageable = PageRequest.of(0, 200);
		List<Product> products = productService.getAllAvailable(pageable).getContent();
		if (refresh)
			productService.refreshProducts(products);
		return ResponseBuilder.createResponse("Refreshed");
	}

	@GetMapping("/v1/products")
	public ResponseDto getAllHateoas(
			@RequestParam(name = "prime") Optional<Boolean> prime,
			@RequestParam(name = "type") Optional<String> type,
			Pageable pageable
	) {
		Page<Product> productDao;
		if (prime.isPresent()) {
			productDao = productService.getAllPrime(pageable);
		} else if (type.isPresent()) {
			productDao = productService.getAllAvailableByType(type.get(), pageable);
		} else {
			productDao = productService.getAllAvailable(pageable);
		}

		PagedModel<ProductHateoasResponseDto> productDto = pagedResourcesAssembler.toModel(productDao,
				productAssembler);
		return ResponseBuilder.createListResponse(productDao.getContent().size(), productDto);
	}
}
