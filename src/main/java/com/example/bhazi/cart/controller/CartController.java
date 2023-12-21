package com.example.bhazi.cart.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bhazi.cart.domain.model.Cart;
import com.example.bhazi.cart.domain.model.CartItem;
import com.example.bhazi.cart.domain.model.CartSubProduct;
import com.example.bhazi.cart.domain.service.CartService;
import com.example.bhazi.cart.dto.CartCreateDto;
import com.example.bhazi.cart.dto.CartDashboardResponseDto;
import com.example.bhazi.cart.dto.CartResponseDto;
import com.example.bhazi.cart.dto.CartScheduleDto;
import com.example.bhazi.cart.mapper.CartMapper;
import com.example.bhazi.core.dto.ResponseDto;
import com.example.bhazi.util.ResponseBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {
	private final CartService cartService;
	private final CartMapper cartMapper;

	@PostMapping("/v1/carts")
	public ResponseDto saveCart(@Valid @RequestBody CartCreateDto cartCreateDto) {
		CartItem savedCartItem;
		int profileId = cartCreateDto.getProfileId();
		CartSubProduct cartSubProduct = cartMapper.mapToDomainCartSubProduct(cartCreateDto);

		savedCartItem = cartService.createCartAndAddCartItem(profileId, cartSubProduct);

		Cart cart = cartService.getCartById(savedCartItem.getCartId());
		CartDashboardResponseDto response = cartMapper
				.mapToResponseCartDashboardDto(cart, savedCartItem);
		return ResponseBuilder.createResponse(response);
	}

	@PutMapping("/v1/carts/{cartId}")
	public ResponseDto updateCart(@PathVariable(name = "cartId") long cartId,
			@RequestParam(name = "deliveryType") Optional<String> deliveryType,
			@RequestParam(name = "packagingType") Optional<String> packagingType,
			@RequestParam(name = "addressId") Optional<Long> addressId,
			@RequestParam(name = "couponCode") Optional<String> couponCode,
			@RequestParam(name = "walletUsed") Optional<Boolean> walletUsed,
			@RequestParam(name = "couponApplied") Optional<Boolean> couponApplied
	) {
		Cart updatedCart;

		if (deliveryType.isPresent()) {
			updatedCart = cartService.updateDeliveryType(cartId, deliveryType.get());
		} else if (packagingType.isPresent()) {
			updatedCart = cartService.updatePackagingType(cartId, packagingType.get());
		} else if (addressId.isPresent()) {
			updatedCart = cartService.updateAddressId(cartId, addressId.get());
		} else if (couponCode.isPresent()) {
			updatedCart = cartService.updateCouponCode(cartId, couponCode.get());
		} else if (walletUsed.isPresent()) {
			updatedCart = cartService.updateWalletUsed(cartId, walletUsed.get());
		} else if (couponApplied.isPresent()) {
			updatedCart = cartService.updateCouponApplied(cartId, couponApplied.get());
		} else {
			updatedCart = cartService.getCartById(cartId);
		}

		CartResponseDto response = cartMapper.mapToResponseCartDto(updatedCart);
		return ResponseBuilder.createResponse(response);
	}

	@PutMapping("/v1/carts/{cartId}/{subProductId}")
	public ResponseDto updateCartItem(@PathVariable(name = "cartId") long cartId,
			@PathVariable(name = "subProductId") int subProductId, @RequestParam(name = "counter") int counter) {
		cartService.updateCartItem(cartId, subProductId, counter);
		Cart cart = cartService.getCartById(cartId);
		CartResponseDto response = cartMapper.mapToResponseCartDto(cart);
		return ResponseBuilder.createResponse(response);
	}

	@GetMapping("/v1/carts/{cartId}")
	public ResponseDto getCartById(@PathVariable(name = "cartId") long cartId) {
		Cart cart = cartService.getCartById(cartId);
		CartResponseDto response = cartMapper.mapToResponseCartDto(cart);
		return ResponseBuilder.createResponse(response);
	}
	
	@GetMapping("/profiles/{profileId}/carts")
	public ResponseDto getCartByProfileId(@PathVariable(name = "profileId") int profileId) {
		Cart cart = cartService.getCartByProfileId(profileId);
		CartResponseDto response = cartMapper.mapToResponseCartDto(cart);
		return ResponseBuilder.createResponse(response);
	}
	
	@GetMapping("/v1/carts/{cartId}/schedule")
	public CartScheduleDto getCartSchedule(@PathVariable(name = "cartId") long cartId,
			@RequestParam(name = "type", defaultValue = "next day") String type) {
		return cartMapper.mapToResponseCartScheduleDto(cartService.getCartSchedule(cartId, type));
	}
}
