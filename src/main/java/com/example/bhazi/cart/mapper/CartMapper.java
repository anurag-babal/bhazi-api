package com.example.bhazi.cart.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.bhazi.cart.domain.model.Cart;
import com.example.bhazi.cart.domain.model.CartItem;
import com.example.bhazi.cart.domain.model.CartSchedule;
import com.example.bhazi.cart.domain.model.CartSubProduct;
import com.example.bhazi.cart.domain.model.DeliveryTimeSlot;
import com.example.bhazi.cart.dto.CartCreateDto;
import com.example.bhazi.cart.dto.CartDashboardResponseDto;
import com.example.bhazi.cart.dto.CartItemDashboardResponseDto;
import com.example.bhazi.cart.dto.CartItemDetailResponseDto;
import com.example.bhazi.cart.dto.CartItemResponseDto;
import com.example.bhazi.cart.dto.CartResponseDto;
import com.example.bhazi.cart.dto.CartScheduleDto;
import com.example.bhazi.cart.dto.CartScheduleTimeSlotResponseDto;
import com.example.bhazi.order.dto.OrderCreateDto;
import com.example.bhazi.order.dto.OrderItemCreateDto;
import com.example.bhazi.product.domain.Product;
import com.example.bhazi.product.domain.ProductService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CartMapper {
	private final ProductService productService;
	
	public CartItem mapToDomainCartItem(CartCreateDto itemDto) {
		CartItem cartItem = new CartItem();
		CartSubProduct cartSubProduct = new CartSubProduct();
		cartSubProduct.setSubProductId(itemDto.getSubProductId());
		cartSubProduct.setCounter(itemDto.getCounter());
		cartItem.setSubProducts(List.of(cartSubProduct));
		return cartItem;
	}
	
	public CartSubProduct mapToDomainCartSubProduct(CartCreateDto itemDto) {
		CartSubProduct cartSubProduct = new CartSubProduct();
		cartSubProduct.setSubProductId(itemDto.getSubProductId());
		cartSubProduct.setCounter(itemDto.getCounter());
		return cartSubProduct;
	}
	
	public CartItemDetailResponseDto mapToResponseSubProductsDto(CartSubProduct cartSubProduct) {
		return CartItemDetailResponseDto.builder()
				.id(cartSubProduct.getId())
				.subProductId(cartSubProduct.getSubProductId())
				.counter(cartSubProduct.getCounter())
				.weight(cartSubProduct.getWeight())
				.price(cartSubProduct.getPrice())
				.active(cartSubProduct.isActive())
				.build();
	}
	
	public List<CartItemDetailResponseDto> mapToResponseSubProductsDtos(List<CartSubProduct> cartSubProducts) {
		if (cartSubProducts == null)
			return new ArrayList<>();
		return cartSubProducts.stream()
				.map(this::mapToResponseSubProductsDto)
				.collect(Collectors.toList());
	}
	
	public CartItemResponseDto mapToResponseCartItemDto(CartItem cartItem) {
		if (cartItem == null) return CartItemResponseDto.builder().build();
		int productId = cartItem.getProductId();
		Product product = productService.getById(productId);
		return CartItemResponseDto.builder()
			.productId(productId)
			.active(cartItem.isActive())
			.name(product.getName())
			.nameHindi(product.getNameHindi())
			.imageUrl(product.getImageUrl())
			.price(cartItem.getPrice())
			.weight(cartItem.getWeight())
			.subProducts(mapToResponseSubProductsDtos(cartItem.getSubProducts()))
			.build();
	}
	
	public List<CartItemResponseDto> mapToResponseCartItemDtos(List<CartItem> cartItems) {
		if (cartItems == null)
			return new ArrayList<>();
		return cartItems.stream()
				.map(this::mapToResponseCartItemDto)
				.collect(Collectors.toList());
	}
	
	public CartResponseDto mapToResponseCartDto(Cart cart) {
		return CartResponseDto.builder()
			.id(cart.getId())
			.active(cart.isActive())
			.couponApplied(cart.isCouponApplied())
			.couponCode(cart.getCouponCode())
			.couponDiscount(cart.getCouponDiscount())
			.deliveryCharge(cart.getDeliveryCharge())
			.deliveryMessage(cart.getDeliveryMessage())
			.addressId(cart.getAddressId())
			.deliveryType(cart.getDeliveryType().getDescription())
			.packagingCharge(cart.getPackagingCharge())
			.packagingType(cart.getPackagingType().getDescription())
			.walletUsed(cart.isWalletUsed())
			.walletBalanceUsed(cart.getWalletBalanceUsed())
			.referralBalanceUsed(cart.getReferralBalanceUsed())
			.referralMessage(cart.getReferralMessage())
			.totalItemsPrice(cart.getTotalItemsPrice())
			.totalDiscount(cart.getTotalDiscount())
			.payable(cart.getPayable())
			.cartItems(mapToResponseCartItemDtos(cart.getCartItems()))
			.build();
	}
	
	public CartScheduleTimeSlotResponseDto mapToCartScheduleTimeSlotResponseDto(DeliveryTimeSlot deliveryTimeSlot) {
		return CartScheduleTimeSlotResponseDto.builder()
				.timeSlot(deliveryTimeSlot.getTimeSlot())
				.deliveryCharge(deliveryTimeSlot.getDeliveryCharge())
				.active(deliveryTimeSlot.isActive())
				.build();
	}
	
	public List<CartScheduleTimeSlotResponseDto> mapToCartScheduleTimeSlotResponseDtos(List<DeliveryTimeSlot> deliveryTimeSlots) {
		if (deliveryTimeSlots == null)
			return new ArrayList<>();
		return deliveryTimeSlots.stream()
				.map(this::mapToCartScheduleTimeSlotResponseDto)
				.collect(Collectors.toList());
	}
	
	public CartScheduleDto mapToResponseCartScheduleDto(CartSchedule cartSchedule) {
		return CartScheduleDto.builder()
				.type(cartSchedule.getScheduleType().getDisplayType())
				.timeSlots(mapToCartScheduleTimeSlotResponseDtos(cartSchedule.getDeliveryTimeSlots()))
				.itemsNotAvailable(mapToResponseCartItemDtos(cartSchedule.getItemsNotAvailable()))
				.build();
	}
	
	// Dashboard response 
	public CartDashboardResponseDto mapToResponseCartDashboardDto(Cart cart, CartItem cartItem) {
		return CartDashboardResponseDto.builder()
			.id(cart.getId())
			.active(cart.isActive())
			.totalItemsPrice(cart.getTotalItemsPrice())
			.cartItem(mapToResponseCartItemDashboardDto(cartItem))
			.build();
	}
	
	public CartItemDashboardResponseDto mapToResponseCartItemDashboardDto(CartItem cartItem) {
		return CartItemDashboardResponseDto.builder()
				.active(cartItem.isActive())
				.productId(cartItem.getProductId())
				.weight(cartItem.getWeight())
				.price(cartItem.getPrice())
				.subProducts(mapToResponseSubProductsDtos(cartItem.getSubProducts()))
				.build();
	}
	
	
	
	
	
	public OrderCreateDto mapToOrderCreateDto(Cart cart) {
		OrderCreateDto orderDto = new OrderCreateDto();
		orderDto.setAmount(cart.getTotalItemsPrice());
		orderDto.setCouponBalance(cart.getCouponDiscount());
		orderDto.setDeliveryCharge(cart.getDeliveryCharge());
		orderDto.setPackagingCharge(cart.getPackagingCharge());
		orderDto.setReferralBalance(cart.getReferralBalanceUsed());
		orderDto.setWalletBalance(cart.getWalletBalanceUsed());
		orderDto.setAddressId(419);
		orderDto.setPaymentMode("COD");
		orderDto.setPackagingType(cart.getPackagingType().getDescription());
		orderDto.setDeliveryType(cart.getDeliveryType().getDescription());
		orderDto.setProfileId(cart.getProfileId());
		orderDto.setOrderItems(mapToOrderItemCreateDtos(cart.getCartItems()));
		return orderDto;
	}
	
	public OrderItemCreateDto mapToOrderItemCreateDto(CartItem cartItem) {
		OrderItemCreateDto orderItem = new OrderItemCreateDto();
		orderItem.setPrice((int) cartItem.getPrice());
		orderItem.setProductId(cartItem.getProductId());
		orderItem.setQuantity(cartItem.getWeight());
		return orderItem;
	}

	public List<OrderItemCreateDto> mapToOrderItemCreateDtos(List<CartItem> cartItems) {
		return cartItems.stream()
				.map(this::mapToOrderItemCreateDto)
				.collect(Collectors.toList());
	}
}
