package com.example.bhazi.cart.data.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.example.bhazi.cart.data.dao.CartDao;
import com.example.bhazi.cart.data.dao.CartItemDao;
import com.example.bhazi.cart.data.entity.CartEntity;
import com.example.bhazi.cart.data.entity.CartItemEntity;
import com.example.bhazi.cart.domain.model.Cart;
import com.example.bhazi.cart.domain.model.CartItem;
import com.example.bhazi.cart.domain.model.CartSubProduct;
import com.example.bhazi.cart.domain.repository.CartRepository;
import com.example.bhazi.core.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepository {
	private final CartDao cartDao;
	private final CartItemDao cartItemDao;

	@Override
	public Cart saveCart(Cart cart) {
		CartEntity savedCartEntity;
		CartEntity cartEntity = mapToEntityCart(cart);
		savedCartEntity = cartDao.save(cartEntity);
		return mapToDomainCart(cartDao.save(savedCartEntity));
	}
	
	@Override
	public Cart updateCart(long id, Cart cart) {
		CartEntity oldCartEntity = getCartEntityById(id);
		CartEntity updatedCartEntity;
		CartEntity cartEntity = mapToEntityCart(cart);
		cartEntity.setCartItems(oldCartEntity.getCartItems());
		cartEntity.setId(id);
		updatedCartEntity = cartDao.save(cartEntity);
		return mapToDomainCart(updatedCartEntity);
	}

	@Override
	public CartItem saveCartSubProduct(long cartId, int productId, CartSubProduct cartSubProduct) {
		CartItemEntity savedCartItemEntity;
		CartItemEntity itemEntity = mapToEntityCartItem(cartId, productId, cartSubProduct);
		savedCartItemEntity = cartItemDao.save(itemEntity);
		return mapToDomainCartItem(savedCartItemEntity);
	}

	@Override
	public CartItem updateCartSubProduct(long cartItemId, long cartId, int productId, CartSubProduct cartSubProduct) {
		CartItemEntity updatedCartItemEntity;
		CartItemEntity itemEntity = mapToEntityCartItem(cartId, productId, cartSubProduct);
		itemEntity.setId(cartItemId);
		updatedCartItemEntity = cartItemDao.save(itemEntity);
		return mapToDomainCartItem(updatedCartItemEntity);
	}
	
	@Override
	public Cart getCartById(long id) {
		CartEntity cartEntity = getCartEntityById(id);
		return mapToDomainCart(cartEntity);
	}
	
	@Override
	public Optional<Cart> getCartByProfileId(int profileId) {
		Optional<CartEntity> cartEntity = cartDao.findByProfileIdAndActive(profileId, true);
		if (cartEntity.isEmpty())
			return Optional.empty();
		return Optional.of(mapToDomainCart(cartEntity.get()));
	}
	
	@Override
	public CartItem getCartItemById(long id) {
		CartItemEntity itemEntity = getCartItemEntityById(id);
		return mapToDomainCartItem(itemEntity);
	}

	@Override
	public Page<CartItem> getCartItemsByCartId(long cartId, Pageable pageable) {
		return cartItemDao.findAllByCartEntityId(cartId, pageable).map(this::mapToDomainCartItem);
	}
	
	private List<CartItemEntity> getAllSubProductsByCartIdAndProductId(long cartId, int productId) {
		return cartItemDao.findAllByCartEntityIdAndProductId(cartId, productId);
	}
	
	private CartEntity getCartEntityById(long id) {
		return cartDao.findById(id).orElseThrow(
			() -> new EntityNotFoundException("Cart not found with id: " + id)
		);
	}
	
	private CartItemEntity getCartItemEntityById(long id) {
		return cartItemDao.findById(id).orElseThrow( 
				() -> new EntityNotFoundException("Cart Item not found with id: " + id) 
		);
	}

	private CartEntity mapToEntityCart(Cart cart) {
		CartEntity cartEntity = new CartEntity();
		cartEntity.setProfileId(cart.getProfileId());
		cartEntity.setActive(cart.isActive());
		cartEntity.setCouponApplied(cart.isCouponApplied());
		cartEntity.setCouponCode(cart.getCouponCode());
		cartEntity.setCouponDiscount(cart.getCouponDiscount());
		cartEntity.setDeliveryCharge(cart.getDeliveryCharge());
		cartEntity.setAddressId(cart.getAddressId());
		cartEntity.setDeliveryType(cart.getDeliveryType());
		cartEntity.setPackagingCharge(cart.getPackagingCharge());
		cartEntity.setPackagingType(cart.getPackagingType());
		cartEntity.setWalletUsed(cart.isWalletUsed());
		cartEntity.setWalletBalanceUsed(cart.getWalletBalanceUsed());
		cartEntity.setReferralBalanceUsed(cart.getReferralBalanceUsed());
		cartEntity.setTotalAmount(cart.getTotalItemsPrice());
		cartEntity.setTotalDiscount(cart.getTotalDiscount());
		cartEntity.setPayable(cart.getPayable());
		return cartEntity;
	}

	private Cart mapToDomainCart(CartEntity cartEntity) {
		Cart cart = new Cart();
		cart.setId(cartEntity.getId());
		cart.setProfileId(cartEntity.getProfileId());
		cart.setActive(cartEntity.isActive());
		cart.setCouponApplied(cartEntity.isCouponApplied());
		cart.setCouponCode(cartEntity.getCouponCode());
		cart.setCouponDiscount(cartEntity.getCouponDiscount());
		cart.setDeliveryCharge(cartEntity.getDeliveryCharge());
		cart.setAddressId(cartEntity.getAddressId());
		cart.setDeliveryType(cartEntity.getDeliveryType());
		cart.setPackagingCharge(cartEntity.getPackagingCharge());
		cart.setPackagingType(cartEntity.getPackagingType());
		cart.setWalletUsed(cartEntity.isWalletUsed());
		cart.setWalletBalanceUsed(cartEntity.getWalletBalanceUsed());
		cart.setReferralBalanceUsed(cartEntity.getReferralBalanceUsed());
		cart.setTotalItemsPrice(cartEntity.getTotalAmount());
		cart.setTotalDiscount(cartEntity.getTotalDiscount());
		cart.setPayable(cartEntity.getPayable());
		cart.setUpdatedAt(cartEntity.getUpdatedAt());
		cart.setCartItems(mapToDomainCartItems(cartEntity.getCartItems()));
		return cart;
	}
	
	private CartItemEntity mapToEntityCartItem(long cartId, int productId, CartSubProduct cartSubProduct) {
//		long cartId = cartItem.getCartId();
		CartItemEntity itemEntity = new CartItemEntity();
		itemEntity.setProductId(productId);
		itemEntity.setSubProductId(cartSubProduct.getSubProductId());
		itemEntity.setQuantity(cartSubProduct.getWeight());
		itemEntity.setCounter(cartSubProduct.getCounter());
		itemEntity.setPrice(cartSubProduct.getPrice());
		itemEntity.setActive(cartSubProduct.isActive());
		itemEntity.setCartEntity(getCartEntityById(cartId));
		return itemEntity;
	}

	private CartItem mapToDomainCartItem(CartItemEntity itemEntity) {
		int productId = itemEntity.getProductId();
		long cartEntityId = itemEntity.getCartEntity().getId();
		List<CartItemEntity> cartItemEntities = getAllSubProductsByCartIdAndProductId(cartEntityId, productId);
		List<CartSubProduct> subProducts = mapToDomainCartSubProducts(cartItemEntities);
		CartItem cartItem = new CartItem();
		
		cartItem.setProductId(productId);
		cartItem.setCartId(itemEntity.getCartEntity().getId());
		
		cartItem.setActive(subProducts.stream().anyMatch(CartSubProduct::isActive));
		cartItem.setPrice((float) subProducts.stream().mapToDouble(CartSubProduct::getPrice).sum());
		cartItem.setWeight(subProducts.stream().mapToInt(CartSubProduct::getWeight).sum());
		
		cartItem.setSubProducts(subProducts);
		return cartItem;
	}
	
	private List<CartItem> mapToDomainCartItems(List<CartItemEntity> items) {
		if (items == null) return new ArrayList<>();
		Map<Integer, List<CartItemEntity>> maps = items.stream()
				.collect(Collectors.groupingBy(CartItemEntity::getProductId));
		return maps.keySet().stream()
				.map(key -> mapToDomainCartItem(maps.get(key).get(0)))
				.collect(Collectors.toList());
	}
	
	private CartSubProduct mapToDomainCartSubProduct(CartItemEntity cartItem) {
		CartSubProduct cartSubProduct = new CartSubProduct();
		cartSubProduct.setActive(cartItem.isActive());
		cartSubProduct.setCounter(cartItem.getCounter());
		cartSubProduct.setPrice(cartItem.getPrice());
		cartSubProduct.setId(cartItem.getId());
		cartSubProduct.setSubProductId(cartItem.getSubProductId());
		cartSubProduct.setWeight(cartItem.getQuantity());
		return cartSubProduct;
	}
	
	private List<CartSubProduct> mapToDomainCartSubProducts(List<CartItemEntity> cartItemEntities) {
		List<CartSubProduct> cartSubProducts = new ArrayList<>();
		cartItemEntities.forEach(item -> cartSubProducts.add(mapToDomainCartSubProduct(item)));
		return cartSubProducts;
	}

	@Override
	public Cart getCartByCartSubProductId(long cartSubProductId) {
		long cartId = getCartItemById(cartSubProductId).getCartId();
		return getCartById(cartId);
	}

	@Override
	public CartSubProduct getCartSubProductById(long cartSubProductId) {
		CartItemEntity cartItem = getCartItemEntityById(cartSubProductId);		
		return mapToDomainCartSubProduct(cartItem);
	}
	
	@Override
	public CartSubProduct getCartSubProductBySubProductId(long cartId, int subProductId) {
		CartItemEntity cartItem = cartItemDao.findByCartEntityIdAndSubProductId(cartId, subProductId);
		return mapToDomainCartSubProduct(cartItem);
	}
}
