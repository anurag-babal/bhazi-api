package com.example.bhazi.cart.service;

import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bhazi.cart.domain.model.Cart;
import com.example.bhazi.cart.domain.model.CartItem;
import com.example.bhazi.cart.domain.model.CartSchedule;
import com.example.bhazi.cart.domain.model.CartSubProduct;
import com.example.bhazi.cart.domain.model.DeliveryTimeSlot;
import com.example.bhazi.cart.domain.model.ScheduleType;
import com.example.bhazi.cart.domain.model.TimeSlot;
import com.example.bhazi.cart.domain.repository.CartRepository;
import com.example.bhazi.cart.domain.service.CartService;
import com.example.bhazi.core.exception.EntityNotFoundException;
import com.example.bhazi.core.exception.GlobalException;
import com.example.bhazi.coupon.domain.CouponService;
import com.example.bhazi.coupon.domain.model.Coupon;
import com.example.bhazi.finance.domain.WalletService;
import com.example.bhazi.order.domain.model.DeliveryType;
import com.example.bhazi.order.domain.model.PackagingType;
import com.example.bhazi.product.domain.ProductService;
import com.example.bhazi.product.domain.SubProduct;
import com.example.bhazi.profile.domain.AddressService;
import com.example.bhazi.referral.domain.ReferralService;
import com.example.bhazi.referral.domain.model.Referral;
import com.example.bhazi.referral.util.ReferralConstants;
import com.example.bhazi.util.DateUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
	private final CartRepository cartRepository;
	private final ProductService productService;
	private final WalletService walletService;
	private final ReferralService referralService;
	private final CouponService couponService;
	private final AddressService addressService;

	@Transactional
	@Override
	public CartItem createCartAndAddCartItem(int profileId, CartSubProduct cartSubProduct) {
		Cart cart;
		CartItem savedCartItem;
		Optional<Cart> oldCart = cartRepository.getCartByProfileId(profileId);
		if (oldCart.isEmpty()) cart = createCart(profileId);
		else cart = oldCart.get();
		savedCartItem = addOrUpdateCartSubProduct(cart, cartSubProduct);
		return savedCartItem;
	}

	private Cart createCart(int profileId) {
		Cart cart = new Cart();
		cart.setProfileId(profileId);
		cart.setAddressId(addressService.getDefaultAddressForProfileId(profileId)
				.orElseThrow(() -> new GlobalException("Add an address before adding an item")).getId());
		return cartRepository.saveCart(cart);
	}
	
	private CartItem addOrUpdateCartSubProduct(Cart cart, CartSubProduct newCartSubProduct) {
		CartItem savedOrUpdatedCartItem;
		List<CartItem> oldCartItems = cart.getCartItems();
		Optional<CartSubProduct> oldCartSubProduct = filterForCartSubProducts(oldCartItems, newCartSubProduct);
		int counter = newCartSubProduct.getCounter();
		if (oldCartSubProduct.isPresent()) {
			savedOrUpdatedCartItem = updateCartItem(cart.getId(), oldCartSubProduct.get().getSubProductId(), counter);
		} else {
			savedOrUpdatedCartItem = addCartSubProduct(cart.getId(), newCartSubProduct, counter);
		}
		Optional<CartSubProduct> cartSubProduct = filterForCartSubProduct(savedOrUpdatedCartItem, newCartSubProduct);
		savedOrUpdatedCartItem.setSubProducts(List.of(
				cartSubProduct.orElseThrow(() -> new EntityNotFoundException("Can't find any matched sub product"))));
		return savedOrUpdatedCartItem;
	}
	
	private Optional<CartSubProduct> filterForCartSubProduct(CartItem cartItem, CartSubProduct cartSubProduct) {
		return cartItem.getSubProducts().stream()
				.filter(subProduct -> subProduct.getSubProductId() == cartSubProduct.getSubProductId())
				.findFirst();
	}
	
	private Optional<CartSubProduct> filterForCartSubProducts(List<CartItem> oldCartItems, CartSubProduct newCartSubProduct) {
		return oldCartItems.stream()
				.flatMap(item -> item.getSubProducts().stream())
				.filter(oldCartSubProduct -> oldCartSubProduct.getSubProductId() == newCartSubProduct.getSubProductId())
				.findFirst();
	}
	
	private CartItem addCartSubProduct(long cartId, CartSubProduct newCartSubProduct, int counter) {
		CartSubProduct initializedCartSubProduct = initCartSubProduct(newCartSubProduct, counter);
		updateCartItemPrice(cartId, initializedCartSubProduct);
		int productId = getSubProduct(newCartSubProduct).getProductId();
		return cartRepository.saveCartSubProduct(cartId, productId, initializedCartSubProduct);
	}
	
	private CartSubProduct initCartSubProduct(CartSubProduct newCartSubProduct, int counter) {
		updateCartItemCounter(newCartSubProduct, counter);
		updateCartItemWeight(newCartSubProduct);
		return newCartSubProduct;
	}
	
	private void updateCartItemPrice(CartItem cartItem) {
		long cartId = cartItem.getCartId();
		cartItem.getSubProducts().stream().forEach(item -> updateCartItemPrice(cartId, item));
	}
	
	private CartSubProduct updateCartItemPrice(long cartId, CartSubProduct cartSubProduct) {
		float basePrice = getSubProduct(cartSubProduct).getPrice();
		float oldPrice = cartSubProduct.getPrice();
		float newPrice = (basePrice * cartSubProduct.getCounter()) - oldPrice;
		cartSubProduct.setPrice(oldPrice + newPrice);
		updateTotalAmount(cartId, newPrice);
		return cartSubProduct;
	}
	
	private Cart updateTotalAmount(long cartId, float amount) {
		Cart cart = getCartById(cartId);
		float updatedAmount = cart.getTotalItemsPrice() + amount;
		if (updatedAmount == 0) cart.setActive(false);
		cart.setTotalItemsPrice(updatedAmount);
		updateReferralBalanceUsed(cart);
		return updateCart(cartId, cart);
	}
	
	@Transactional
	@Override
	public CartItem updateCartItem(long cartId, int subProductId, int counter) {
		CartSubProduct cartSubProduct = cartRepository.getCartSubProductBySubProductId(cartId, subProductId);
		
		if (cartItemCounterRepeat(cartSubProduct, counter))
			return getCartItemById(cartSubProduct.getId());

		int productId = getSubProduct(cartSubProduct).getProductId();
		updateCartItemCounter(cartSubProduct, counter);
		updateCartItemWeight(cartSubProduct);
		updateCartItemPrice(cartId, cartSubProduct);
		
		return cartRepository.updateCartSubProduct(cartSubProduct.getId(), cartId, productId, cartSubProduct);
	}

	private boolean cartItemCounterRepeat(CartSubProduct cartSubProduct, int counter) {
		return cartSubProduct.getCounter() == counter;
	}
	
	private void updateCartItemCounter(CartSubProduct cartSubProduct, int counter) {
		if (counter == 0) cartSubProduct.setActive(false);
		else if (counter == 1) cartSubProduct.setActive(true);
		cartSubProduct.setCounter(counter);
	}

	private void updateCartItemWeight(CartSubProduct cartSubProduct) {
		int baseWeight = getSubProduct(cartSubProduct).getWeight();
		int updatedWeight = baseWeight * cartSubProduct.getCounter();
		cartSubProduct.setWeight(updatedWeight);
	}
	
	private SubProduct getSubProduct(CartSubProduct cartSubProduct) {
		return productService.getSubProductById(cartSubProduct.getSubProductId());
	}

	@Transactional
	@Override
	public Cart updateCart(long id, Cart cart) {
		return cartRepository.updateCart(id, cart);
	}

	@Override
	public Cart getCartById(long id) {
		return cartRepository.getCartById(id);
	}
	
	@Override
	public Cart getCartByProfileId(int profileId) {
		return updateCartWithLatestPrices(cartRepository.getCartByProfileId(profileId)
				.orElseThrow( () -> new EntityNotFoundException("No active cart present")));
	}
	
	private Cart updateCartWithLatestPrices(Cart cart) {
		Cart updatedCart = cart;
		Instant currentInstant = DateUtil.getTimestamp();
		if (DateUtil.differenceBetweenDays(currentInstant, cart.getUpdatedAt()) != 0) {
			cart.getCartItems().stream().forEach(this::updateCartItemPrice);
			updatedCart = cartRepository.getCartById(cart.getId());
		}
		return updatedCart;
	}

	private CartItem getCartItemById(long id) {
		return cartRepository.getCartItemById(id);
	}

	@Override
	public Cart updatePackagingType(long cartId, String packagingType) {
		Cart cart = getCartById(cartId);
		PackagingType updatedPackagingType = PackagingType.valueOf(packagingType.replace(" ", "_").toUpperCase());
		cart.setPackagingType(updatedPackagingType);
		cart.setPackagingCharge(updatedPackagingType.getCharge());
		updatePayable(cart);
		return cartRepository.updateCart(cartId, cart);
	}
	
	@Override
	public Cart updateWalletUsed(long cartId, boolean walletUsed) {
		Cart cart = getCartById(cartId);
		float walletBalanceUsed;
		if (walletUsed) {
			int profileId = cart.getProfileId();
			float payable = cart.getPayable();
			float walletBalance = walletService.getByProfileId(profileId).getBalance();
			if (walletBalance >= payable) {
				walletBalanceUsed = payable;
			} else {
				walletBalanceUsed = walletBalance;
			}
		} else {
			walletBalanceUsed = 0;
		}
		cart.setWalletUsed(walletUsed);
		cart.setWalletBalanceUsed(walletBalanceUsed);
		updatePayable(cart);
		return cartRepository.updateCart(cartId, cart);
	}
	
	private void updatePayable(Cart cart) {
		float payable = cart.getTotalItemsPrice();
		payable += cart.getDeliveryCharge();
		payable += cart.getPackagingCharge();
		payable -= cart.getCouponDiscount();
		payable -= cart.getReferralBalanceUsed();
		payable -= cart.getWalletBalanceUsed();
		cart.setPayable(payable);
	}
	
	private void updateReferralBalanceUsed(Cart cart) {
		int profileId = cart.getProfileId();
		Referral referral = referralService.getByProfileId(cart.getProfileId());
		int verifiedUser = (int) referralService.getAllChildren(profileId).stream()
				.filter(Referral::isVerified).count();
		float referralBalance = referral.getBalance();
		float allowedPercentage = getReferralPercentAllowed(cart, verifiedUser);
		float discount = cart.getTotalItemsPrice() * allowedPercentage;
		float referralBalanceUsed;
		if (referralBalance >= discount) {
			referralBalanceUsed = discount;
		} else {
			referralBalanceUsed = referralBalance;
		}
		cart.setReferralBalanceUsed(referralBalanceUsed);
		updateTotalDiscount(cart);
		updatePayable(cart);
	}
	
	private float getReferralPercentAllowed(Cart cart, int verifiedUser) {
		float allowedPercentage;
		int a;
		int discount;
		
		if (verifiedUser < ReferralConstants.SILVER) {
			allowedPercentage = 10;
			a = ReferralConstants.SILVER - verifiedUser;
			discount = 30;
		} else if (verifiedUser < ReferralConstants.GOLDEN) {
			allowedPercentage = 30;
			a = ReferralConstants.GOLDEN - verifiedUser;
			discount = 50;
		} else if (verifiedUser < ReferralConstants.PLATINUM) {
			allowedPercentage = 50;
			a = ReferralConstants.PLATINUM - verifiedUser;
			discount = 70;
		} else if (verifiedUser < ReferralConstants.DIAMOND){
			allowedPercentage = 70;
			a = ReferralConstants.DIAMOND - verifiedUser;
			discount = 100;
		} else {
			allowedPercentage = 100;
			a = 0;
			discount = 100;
		}
		cart.setReferralMessage(String.format(ReferralConstants.REFERRAL_MESSAGE, a, discount));
		return (allowedPercentage / 100);
	}
	
	private void updateTotalDiscount(Cart cart) {
		float discount = cart.getReferralBalanceUsed();
		discount += cart.getCouponDiscount();
		cart.setTotalDiscount(discount);
	}

	@Override
	public Cart updateCouponApplied(long cartId, boolean couponApplied) {
		Cart cart = getCartById(cartId);
		if (couponApplied)
			return cart;
		cart.setCouponApplied(couponApplied);
		cart.setCouponCode("");
		cart.setCouponDiscount(0);
		updateTotalDiscount(cart);
		updatePayable(cart);
		return cartRepository.updateCart(cartId, cart);
	}
	
	@Override
	public Cart updateCouponCode(long cartId, String couponCode) {
		Cart cart = getCartById(cartId);
		float cartAmount = cart.getTotalItemsPrice();
		Coupon coupon = couponService.getById(couponCode.toUpperCase());
		float percent = (coupon.getDiscountPercent() / 100.0f);
		int maxDiscount = coupon.getDiscountLimit();
		int minAmount = coupon.getMinAmount();
		if (cartAmount < minAmount) 
			return cart;
		float discountAmount = cartAmount * percent;
		if (discountAmount > maxDiscount)
			discountAmount = maxDiscount;
		cart.setCouponApplied(true);
		cart.setCouponCode(couponCode.toUpperCase());
		cart.setCouponDiscount(discountAmount);
		updateTotalDiscount(cart);
		updatePayable(cart);
		return cartRepository.updateCart(cartId, cart);
	}
	
	@Override
	public Cart updateAddressId(long cartId, long addressId) {
		Cart cart = getCartById(cartId);
		cart.setAddressId(addressId);
		return cartRepository.updateCart(cartId, cart);
	}
	
	@Override
	public Cart updateDeliveryType(long cartId, String deliveryType) {
		Cart cart = getCartById(cartId);
		DeliveryType updatedDeliveryType = DeliveryType.valueOf(deliveryType.replace(" ", "_").toUpperCase());
		cart.setDeliveryType(updatedDeliveryType);
		cart.setDeliveryCharge(updatedDeliveryType.getCharge());
		updatePayable(cart);
		return cartRepository.updateCart(cartId, cart);
	}
	
	@Override
	public CartSchedule getCartSchedule(long cartId, String type) {
		CartSchedule cartSchedule;
		ScheduleType deliveryDay = ScheduleType.valueOf(type.replace(" ", "_").toUpperCase());
		switch(deliveryDay) {
		case SAME_DAY:
			cartSchedule = getSameDayTimeSlots(cartId);
			break;
		case INSTANT:
			cartSchedule = getInstantTimeSlot(cartId);
			break;
		case NEXT_DAY:
			cartSchedule = getNextDayTimeSlots();
			break;
		default:
			cartSchedule = getNextDayTimeSlots();
		}
		return cartSchedule;
	}
	
	private CartSchedule getNextDayTimeSlots() {
		List<TimeSlot> timeSlots = getTimeSlots();
		List<DeliveryTimeSlot> deliveryTimeSlots = new ArrayList<>();
		DeliveryTimeSlot deliveryTimeSlot;
		for (TimeSlot timeSlot : timeSlots) {
			deliveryTimeSlot = new DeliveryTimeSlot();
			deliveryTimeSlot.setActive(true);
			deliveryTimeSlot.setTimeSlot(timeSlot.getDisplayTime());
			deliveryTimeSlots.add(deliveryTimeSlot);
		}
		return CartSchedule.builder()
				.deliveryTimeSlots(deliveryTimeSlots)
				.scheduleType(ScheduleType.NEXT_DAY)
				.build();
	}
	
	private CartSchedule getSameDayTimeSlots(long cartId) {
		Cart cart = getCartById(cartId);
//		double distance = addressService.calculateDistance(cart.getAddressId());
		List<CartItem> itemsNotAvailable = cart.getCartItems().stream()
				.filter(item -> productService.getById(item.getProductId()).isOutOfStock())
				.collect(Collectors.toList());
		
		LocalTime plusTwoHour = DateUtil.getCurrentTime().plusHours(2);
		List<TimeSlot> timeSlots = getTimeSlots();
		List<DeliveryTimeSlot> deliveryTimeSlots = new ArrayList<>();
		DeliveryTimeSlot deliveryTimeSlot;
		float maxDeliveryChargeForSameDay = 30;
		int i = 0;
		for (TimeSlot timeSlot : timeSlots) {
			deliveryTimeSlot = new DeliveryTimeSlot();
			deliveryTimeSlot.setTimeSlot(timeSlot.getDisplayTime());
			if (isTimeSlotAllowed(plusTwoHour, timeSlot)) {
				deliveryTimeSlot.setActive(true);
				deliveryTimeSlot.setDeliveryCharge(maxDeliveryChargeForSameDay - (i++ * 5));
			}
			deliveryTimeSlots.add(deliveryTimeSlot);
		}
		
		return CartSchedule.builder()
				.scheduleType(ScheduleType.SAME_DAY)
				.itemsNotAvailable(itemsNotAvailable)
				.deliveryTimeSlots(deliveryTimeSlots)
				.build();
	}
	
	private List<TimeSlot> getTimeSlots() {
		return Arrays.asList(TimeSlot.values());
	}
	
	private boolean isTimeSlotAllowed(LocalTime time, TimeSlot timeSlot) {
		return time.isBefore(timeSlot.getTime());
	}
	
	private CartSchedule getInstantTimeSlot(long cartId) {
		Cart cart = getCartById(cartId);
		float deliveryChargeForInstant = 35;
//		double distance = addressService.calculateDistance(cart.getAddressId());
		List<CartItem> itemsNotAvailable = cart.getCartItems().stream()
				.filter(item -> productService.getById(item.getProductId()).isOutOfStock())
				.collect(Collectors.toList());
		
		LocalTime currentTime = DateUtil.getCurrentTime();
		String expectedStartTime = currentTime.plusMinutes(40).format(DateTimeFormatter.ofPattern("HH:mm"));
		String expectedEndTime = currentTime.plusMinutes(60).format(DateTimeFormatter.ofPattern("HH:mm"));
		
		DeliveryTimeSlot deliveryTimeSlot = new DeliveryTimeSlot();
		deliveryTimeSlot.setTimeSlot(expectedStartTime + " - " + expectedEndTime);
		deliveryTimeSlot.setActive(true);
		deliveryTimeSlot.setDeliveryCharge(deliveryChargeForInstant);
		
		return CartSchedule.builder()
				.scheduleType(ScheduleType.INSTANT)
				.itemsNotAvailable(itemsNotAvailable)
				.deliveryTimeSlots(List.of(deliveryTimeSlot))
				.build();
	}
}
