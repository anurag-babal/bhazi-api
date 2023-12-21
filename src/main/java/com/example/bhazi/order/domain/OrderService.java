package com.example.bhazi.order.domain;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bhazi.admin.domain.FirebaseMessagingService;
import com.example.bhazi.admin.domain.ShopService;
import com.example.bhazi.admin.domain.model.BhaziMessage;
import com.example.bhazi.admin.domain.model.Shop;
import com.example.bhazi.core.exception.EntityNotFoundException;
import com.example.bhazi.core.exception.GlobalException;
import com.example.bhazi.finance.domain.WalletService;
import com.example.bhazi.finance.domain.model.PaymentMode;
import com.example.bhazi.finance.domain.model.Wallet;
import com.example.bhazi.finance.domain.model.WalletTransaction;
import com.example.bhazi.order.domain.model.Order;
import com.example.bhazi.order.domain.model.OrderItem;
import com.example.bhazi.order.domain.model.OrderStatus;
import com.example.bhazi.product.domain.Product;
import com.example.bhazi.product.domain.ProductService;
import com.example.bhazi.profile.domain.model.Address;
import com.example.bhazi.profile.domain.model.Profile;
import com.example.bhazi.referral.domain.ReferralService;
import com.example.bhazi.referral.domain.model.Referral;
import com.example.bhazi.referral.domain.model.ReferralTransaction;
import com.example.bhazi.util.Distance;
import com.google.firebase.messaging.FirebaseMessagingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final WalletService walletService;
    private final ReferralService referralService;
    private final ShopService shopService;
    private final ProductService productService;
    private final FirebaseMessagingService firebaseMessagingService;

    private static final String DESCRIPTION = "Debited for order: ";

    @Transactional
    public Order save(Order order) {
        Order savedOrder;
        Profile profile = order.getProfile();
        order.setStatus(OrderStatus.ORDERED);
        order.setShop(
            shopService.getById(getNearestShopId(order.getAddress()))
        );
        order.setPayable(caluclatePayable(order));
        savedOrder = orderRepository.save(order);
        long orderNumber = savedOrder.getId();
        checkAndUpdateWallet(order.getWalletBalance(), orderNumber, profile);
        checkAndUpdateReferral(order.getReferralBalance(), orderNumber, profile);
        sendNotification(savedOrder);
        return savedOrder;
    }

    private float caluclatePayable(Order order) {
        // To get a precision upto 2 decimal places,
        // first multiply by 100.0 (double)
        // then divide by 100.0f (to make float)
        float payable = Math.round(
            (
                order.getAmount() - (
                    order.getCouponBalance() + 
                    order.getReferralBalance() + 
                    order.getWalletBalance()
                )
            ) * 100.0
        ) / 100.0f;
        return payable;
    }

    private void checkAndUpdateWallet(float amount, long orderNumber, Profile profile) {
        Wallet wallet = profile.getWallet();
        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setStatus(true);
        walletTransaction.setDescription(DESCRIPTION + orderNumber);
        if (amount > 0) {
            if (wallet.getBalance() < amount)
                throw new GlobalException("Insufficient Wallet balance!");
            walletTransaction.setAmount(-amount);
            walletTransaction.setWallet(wallet);
            walletService.saveTransaction(walletTransaction, profile.getUserId());
        }
    }

    private void checkAndUpdateReferral(float amount, long orderNumber, Profile profile) {
        Referral referral = profile.getReferral();
        ReferralTransaction referralTransaction = new ReferralTransaction();
        referralTransaction.setStatus(true);
        referralTransaction.setDescription(DESCRIPTION + orderNumber);
        if (amount > 0) {
            if (referral.getBalance() < amount)
                throw new GlobalException("Insufficient Referral balance!");
            referralTransaction.setAmount(-amount);
            referralTransaction.setReferral(referral);
            referralService.saveTransaction(referralTransaction);
        }
    }

    private byte getNearestShopId(Address address) {
        double lattitude = address.getLattitude().doubleValue();
        double longitude = address.getLongitude().doubleValue();
        List<Shop> shopList = shopService.getAll();
        byte shopId = 1;
        double distance = Double.MAX_VALUE;
        for (Shop shop : shopList) {
            double newDistance = Distance.distance(
                lattitude, 
                longitude, 
                shop.getLattitude().doubleValue(),
                shop.getLongitude().doubleValue()
            );
            if (newDistance < distance) {
                distance = newDistance;
                shopId = shop.getId();
            }
        }
        return shopId;
    }

    @Transactional
    public Order update(long id, OrderStatus status) {
        Order order = getById(id);
        Profile profile = order.getProfile();
        Referral referral = profile.getReferral();

        if(orderNotUpdatable(order)) {
            throw new GlobalException("This order can't be updated");
        }
        order.setStatus(status);
        sendNotification(order);
        if (status == OrderStatus.DELIVERED) {
            float orderBasePrice = calculateOrderBasePrice(order);
            referralService.distributeMoneyShare(orderBasePrice, referral);
            referralService.updateVerified(referral);
        } else if (status == OrderStatus.CANCELLED) {
            float moneyToBeReturned = 0;
            long orderNumber = order.getId();

            moneyToBeReturned = order.getWalletBalance();
            if (moneyToBeReturned > 0) {
                if (paymentAlreadyPaidForOrder(order)) {
                    moneyToBeReturned += order.getPayable();
                }
                insetWalletTransaction(moneyToBeReturned, orderNumber, profile);
            }

            moneyToBeReturned = order.getReferralBalance();
            if (moneyToBeReturned > 0) {
                insetReferralTransaction(moneyToBeReturned, orderNumber, referral);
            }
        }
        return orderRepository.save(order);
    }

    private void sendNotification(Order order) {
        BhaziMessage bhaziMessage = BhaziMessage.builder()
            .title("Order update")
            .body("Your order no: " + order.getId() + " is " + order.getStatus().getDescription())
            .build();
        String firebaseToken = order.getProfile().getFirebaseToken();

        try {
        	if (firebaseToken != null) {        		
        		firebaseMessagingService.sendNotification(bhaziMessage, firebaseToken);
        	}
        } catch (FirebaseMessagingException e) {
            log.info("Notification_Firebase_Order: {}" + e.getLocalizedMessage());
        }
    }

    private boolean paymentAlreadyPaidForOrder(Order order) {
        PaymentMode paymentMode = order.getPaymentMode();
        if (
            (paymentMode == PaymentMode.PREPAID) ||
            (paymentMode == PaymentMode.ONLINE)
        ) {
            return true;
        }
        return false;
    }

    private float calculateOrderBasePrice(Order order) {
    	float amount = 0;
    	List<OrderItem> items = order.getOrderItems();
    	for (OrderItem item : items) {
    		Product product = item.getProduct();
    		int basePrice = product.getBasePrice();
			float baseQuantity = Integer.parseInt(product.getBaseQuantity().split(" ")[0]);
			if (baseQuantity < 10) baseQuantity *= 1000;
			amount += (basePrice * (item.getQuantity() / baseQuantity));
		}
		return amount;
    }

    private boolean orderNotUpdatable(Order order) {
        OrderStatus oldStatus = order.getStatus();
        if (oldStatus == OrderStatus.DELIVERED || oldStatus == OrderStatus.CANCELLED) {
            return true;
        }
        return false;
    }

    private void insetWalletTransaction(float amount, long orderNumber, Profile profile) {
        String description = "Refund for cancelled order: " + orderNumber;
        Wallet wallet = profile.getWallet();
        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setStatus(true);
        walletTransaction.setDescription(description);
        walletTransaction.setAmount(amount);
        walletTransaction.setWallet(wallet);
        walletService.saveTransaction(walletTransaction, profile.getUserId());
    }

    private void insetReferralTransaction(float amount, long orderNumber, Referral referral) {
        String description = "Refund for cancelled order: " + orderNumber;
        ReferralTransaction referralTransaction = new ReferralTransaction();
        referralTransaction.setStatus(true);
        referralTransaction.setDescription(description);
        referralTransaction.setAmount(amount);
        referralTransaction.setReferral(referral);
        referralService.saveTransaction(referralTransaction);
    }

    @Transactional
    public boolean delete(long id) {
        Order order = getById(id);
        orderRepository.delete(order);
        return true;
    }

    public List<Order> getAllByDateAndStatus(LocalDate date, String status) {
        List<Order> orders = orderRepository.findAllByOrderDateAndStatus(
            date,
            OrderStatus.valueOf(status.replace(" ", "_").toUpperCase())
        );
        return orders;
    }

    public List<Order> getAllByDate(LocalDate date) {
        List<Order> orders = orderRepository.findAllByOrderDate(date);
        return orders;
    }

    public Order getById(long id) {
        Order order = orderRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Order not foudn with id = " + id)
        );
        return order;
    }

    public Page<Order> getByProfileId(int profileId, Pageable pageable) {
        return orderRepository.findByProfileId(profileId, pageable);
    }

    public Order getLatestOrderForProfileId(int profileId) {
    	return orderRepository.findTopByProfileIdOrderByIdDesc(profileId).orElse(null);
    }

    // TODO: Deprecated
    public Page<Order> getAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
}
