package com.example.bhazi.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.example.bhazi.admin.ShopMapper;
import com.example.bhazi.admin.domain.ShopService;
import com.example.bhazi.admin.domain.model.Shop;
import com.example.bhazi.finance.domain.model.PaymentMode;
import com.example.bhazi.order.domain.model.DeliveryTimePref;
import com.example.bhazi.order.domain.model.DeliveryType;
import com.example.bhazi.order.domain.model.Order;
import com.example.bhazi.order.domain.model.OrderItem;
import com.example.bhazi.order.domain.model.OrderStatus;
import com.example.bhazi.order.domain.model.PackagingType;
import com.example.bhazi.order.dto.OrderCreateDto;
import com.example.bhazi.order.dto.OrderItemCreateDto;
import com.example.bhazi.order.dto.OrderItemResponseDto;
import com.example.bhazi.order.dto.OrderResponseDto;
import com.example.bhazi.order.dto.OrderUpdateDto;
import com.example.bhazi.order.dto.admin.AdminOrderDetailResponse;
import com.example.bhazi.order.dto.admin.AdminOrderItemDetailResponse;
import com.example.bhazi.order.dto.admin.DeliveryOrderCountDto;
import com.example.bhazi.order.dto.admin.ItemWiseQuantityResponseDto;
import com.example.bhazi.order.dto.admin.TotalOrderQuantityResponseDto;
import com.example.bhazi.product.domain.Product;
import com.example.bhazi.product.domain.ProductService;
import com.example.bhazi.profile.AddressMapper;
import com.example.bhazi.profile.ProfileMapper;
import com.example.bhazi.profile.domain.model.Address;
import com.example.bhazi.profile.domain.model.Profile;
import com.example.bhazi.util.DateUtil;
import com.example.bhazi.util.Utility;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final ProductService productService;
    private final ShopService shopService;
    private final AddressMapper addressMapper;
    private final ProfileMapper profileMapper;
    private final ShopMapper shopMapper;

    private float totalWeight;

    public Order mapToDomain(OrderCreateDto orderCreateDto, Profile profile, Address address ) {
        updateOrderDetails(orderCreateDto);
        Order order = new Order();
        order.setAmount(orderCreateDto.getAmount());
        order.setDeliveryCharge(orderCreateDto.getDeliveryCharge());
        order.setPackagingCharge(orderCreateDto.getPackagingCharge());
        order.setCouponBalance(orderCreateDto.getCouponBalance());
        order.setReferralBalance(orderCreateDto.getReferralWalletBalance());
        order.setWalletBalance(orderCreateDto.getWalletBalance());
        order.setOrderDate(DateUtil.getDate());
        order.setDeliveryTimePref(getDeliveryTimePref(orderCreateDto.getDeliveryTimePref()));
        order.setPaymentMode(
            PaymentMode.valueOf(orderCreateDto.getPaymentMode().replace(" ", "_").toUpperCase())
        );
        order.setPackagingType(
            PackagingType.valueOf(orderCreateDto.getPackagingType().replace(" ", "_").toUpperCase())
        );
        order.setDeliveryType(
            DeliveryType.valueOf(orderCreateDto.getDeliveryType().replace(" ", "_").toUpperCase())
        );

        order.setAddress(address);
        order.setProfile(profile);

        order.setOrderItems(orderCreateDto.getOrderItems().stream()
                .map(item -> mapToDomainOrderItem(item, order))
                .collect(Collectors.toList()));

        return order;
    }

    private DeliveryTimePref getDeliveryTimePref(String time) {
        return Stream.of(DeliveryTimePref.values())
                .filter(deliveryTime -> deliveryTime.getDescription().equals(time))
                .findFirst()
                .orElse(DeliveryTimePref.ALL_DAY);
    }

    private OrderItem mapToDomainOrderItem(OrderItemCreateDto orderItemCreateDto, Order order) {
        OrderItem orderItem = new OrderItem();
        // Product product = productService.getByName(orderItemCreateDto.getProductName());
        Product product = productService.getById(orderItemCreateDto.getProductId());
        orderItem.setQuantity(orderItemCreateDto.getQuantity());
        orderItem.setPrice(orderItemCreateDto.getPrice());
        orderItem.setProductId(product.getId());
        orderItem.setProduct(product);
        orderItem.setOrder(order);
        return orderItem;
    }

    public OrderResponseDto mapToResponseDto(Order order) {
        if (order == null) {
            return null;
        }
        Profile profile = order.getProfile();
        Shop shop = order.getShop();
        return OrderResponseDto.builder()
                .id(order.getId())
                .status(order.getStatus().getDescription())
                .paybale(order.getPayable())
                .deliveryCharge(order.getDeliveryCharge())
                .packagingCharge(order.getPackagingCharge())
                .couponBalance(order.getCouponBalance())
                .walletBalance(order.getWalletBalance())
                .referralBalance(order.getReferralBalance())
                .amount(order.getAmount())
                .paymentMode(order.getPaymentMode().getDescription())
                .deliveryType(order.getDeliveryType().getDescription())
                .packagingType(order.getPackagingType().getDescription())
                .addressNew(addressMapper.mapToBasicDto(order.getAddress()))
                .deliveryTimePref(order.getDeliveryTimePref().getDescription())
                .shopAddress(shop.getAddress())
                .profile(profileMapper.mapToBasicProfileDto(profile))
                .orderedAt(DateUtil.getTimestampString(order.getCreatedAt()))
                .orderItems(mapToOrderItemResponseDtos(order.getOrderItems()))
                .shop(shopMapper.mapToBasicDto(shop))
                .customerName(profile.getFirstName() + " " + profile.getLastName())
                .customerNumber(profile.getPhoneNumber())
                .profileId(profile.getId())
                .shopId(shop.getId())
                .addressId(order.getAddress().getId())
                .address(order.getAddress().getFullAddress())
                .referralWalletBalance(order.getReferralBalance())
                .packagingId((byte)(order.getPackagingType() == PackagingType.BASIC ? 1 : 2))
                .deliveryId((byte)(order.getDeliveryType() == DeliveryType.HOME_DELIVERY ? 1 : 2))
                .timestamp(DateUtil.getTimestampString(order.getCreatedAt()))
                .build();
    }

    private OrderItemResponseDto mapToOrderItemResponseDto(OrderItem orderItem) {
        return OrderItemResponseDto.builder()
                .id(orderItem.getId())
                .price(orderItem.getPrice())
                .quantity(orderItem.getQuantity())
                .productName(orderItem.getProduct().getName())
                .productId(orderItem.getProductId())
                .build();
    }

    private List<OrderItemResponseDto> mapToOrderItemResponseDtos(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> mapToOrderItemResponseDto(item))
                .collect(Collectors.toList());
    }

    public List<OrderResponseDto> mapToResponseDtos(List<Order> orders) {
        return orders.stream()
                .map(order -> mapToResponseDto(order))
                .collect(Collectors.toList());
    }

    /* @Mapping(target = "status", source = "orderUpdateDto.status")
    public abstract Order mapToDomain(
        OrderUpdateDto orderUpdateDto, Order order
    ); */

    public OrderStatus mapToDomain(OrderUpdateDto orderUpdateDto) {
        /* order.setStatus(OrderStatus.valueOf(
            orderUpdateDto.getStatus().replace(" ", "_").toUpperCase()
        )); */
        return OrderStatus.valueOf(
            orderUpdateDto.getStatus().replace(" ", "_").toUpperCase()
        );
    }

    public List<TotalOrderQuantityResponseDto> totalOrderQuantityResponseDto(List<Order> orderList) {
        List<TotalOrderQuantityResponseDto> responseDto = new ArrayList<>();
        List<Shop> shopList = shopService.getAll();

        for (Shop shop : shopList) {
            List<Order> shopOrderList = orderList.stream()
                    .filter(order -> order.getShop().getId() == shop.getId())
                    .collect(Collectors.toList());

            responseDto.add(new TotalOrderQuantityResponseDto(
                shop.getTag(), getItemList(shopOrderList),shopOrderList.size(), totalWeight
            ));
        }

        responseDto.add(new TotalOrderQuantityResponseDto(
            "Mandi", getItemList(orderList), orderList.size(), totalWeight
        ));

        return responseDto;
    }

    private List<ItemWiseQuantityResponseDto> getItemList (List<Order> orderList) {
        List<ItemWiseQuantityResponseDto> itemDto = new ArrayList<>();
        Map<Integer, Float> map = new HashMap<>();

        orderList.forEach(order -> {
            order.getOrderItems().forEach(item -> {
                int productId = item.getProductId();
                float quantity = item.getQuantity() / 1000f;
                if (map.containsKey(productId))
                    quantity += map.get(productId);
                map.put(productId, quantity);
            });
        });

        map.forEach((key, value) -> {
            String productName = productService.getById(key).getName();
            itemDto.add(new ItemWiseQuantityResponseDto(
                key, productName, value
            ));
        });
        totalWeight = Utility.getSumOfList(map.values());

        return itemDto;
    }

    public DeliveryOrderCountDto deliveryOrderCountDto(List<Order> orders) {
        DeliveryOrderCountDto response = new DeliveryOrderCountDto();
        Map<String, Integer> map = new HashMap<>();
        Map<String, Map<String, Integer>> tmp = new HashMap<>();

        List<Shop> shopList = shopService.getAll();
        for (Shop shop : shopList) {
            Map<String, Integer> tmpMap = new HashMap<>();
            List<Order> shopOrderList = orders.stream()
                    .filter(order -> order.getShop().getId() == shop.getId())
                    .collect(Collectors.toList());

            int count = shopOrderList.stream()
                    .filter(order -> order.getStatus() == OrderStatus.ORDERED)
                    .collect(Collectors.toList()).size();
            tmpMap.put("Ordered", count);
    
            count = shopOrderList.stream()
                    .filter(order -> order.getStatus() == OrderStatus.CANCELLED)
                    .collect(Collectors.toList()).size();
            tmpMap.put("Cancelled", count);
    
            count = shopOrderList.stream()
                    .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                    .collect(Collectors.toList()).size();
            tmpMap.put("Delivered", count);
            
            count = shopOrderList.stream()
                    .filter(order -> 
                        order.getStatus() == OrderStatus.OUT_FOR_DELIVERY
                    )
                    .collect(Collectors.toList()).size();
            tmpMap.put("Out for delivery", count);

            tmp.put(shop.getTag(), tmpMap);
        }
        response.setTmp(tmp);

        int count = orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.ORDERED)
                .collect(Collectors.toList()).size();
        map.put("Ordered", count);

        count = orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.CANCELLED)
                .collect(Collectors.toList()).size();
        map.put("Cancelled", count);

        count = orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .collect(Collectors.toList()).size();
        map.put("Delivered", count);
        
        count = orders.stream()
                .filter(order -> 
                    order.getStatus() == OrderStatus.OUT_FOR_DELIVERY
                )
                .collect(Collectors.toList()).size();
        map.put("Out for delivery", count);

        response.setDelivery(map);
        return response;
    }

    public DeliveryOrderCountDto orderCountDto(List<Order> orders) {
        DeliveryOrderCountDto response = new DeliveryOrderCountDto();
        Map<String, Integer> result = new HashMap<>();
        Map<String, Float> price = new HashMap<>();
        float total = 0.0f;
        List<Order> orderedList = orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.ORDERED)
                .collect(Collectors.toList());

        int count = orderedList.size();
        result.put("Ordered", count);
        count = orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.CANCELLED)
                .collect(Collectors.toList()).size();
        result.put("Cancelled", count);

        List<Order> cashOrders = orderedList.stream()
                .filter(order -> order.getPaymentMode() == PaymentMode.COD)
                .collect(Collectors.toList());
        for (Order order : cashOrders) {
            total += order.getAmount();
        }
        price.put("Postpaid", (float)Utility.scaleNumber(total, 2));

        total = 0.0f;
        cashOrders = orderedList.stream()
                .filter(order -> order.getPaymentMode() == PaymentMode.PREPAID)
                .collect(Collectors.toList());
        for (Order order : cashOrders) {
            total += order.getAmount();
        }
        price.put("Prepaid", (float)Utility.scaleNumber(total, 2));

        response.setDelivery(result);
        response.setPrice(price);
        return response;
    }

    public List<AdminOrderDetailResponse> adminOrderDetailResponses(List<Order> orderList) {
        List<AdminOrderDetailResponse> response = new ArrayList<>();
        List<AdminOrderItemDetailResponse> items;
        Profile profile;

        for (Order order : orderList) {
            items = new ArrayList<>();
            for (OrderItem item : order.getOrderItems()) {
                items.add(new AdminOrderItemDetailResponse(
                    item.getId(),
                    productService.getById(item.getProductId()).getName(),
                    item.getQuantity(),
                    item.getPrice()
                ));
            }
            profile = order.getProfile();
            response.add(AdminOrderDetailResponse.builder()
                    .id(order.getId())
                    .status(order.getStatus().getDescription())
                    .amount(order.getAmount())
                    .deliveryCharge(order.getDeliveryCharge())
                    .packagingCharge(order.getPackagingCharge())
                    .walletBalance(order.getWalletBalance())
                    .couponBalance(order.getCouponBalance())
                    .referralWalletBalance(order.getReferralBalance())
                    .paymentMode(order.getPaymentMode().getDescription())
                    .deliveryType(order.getDeliveryType().getDescription())
                    .packagingType(order.getPackagingType().getDescription())
                    .deliveryTimePref(order.getDeliveryTimePref().getDescription())
                    .address(order.getAddress().getFullAddress())
                    .addressNew(addressMapper.mapToDto(order.getAddress()))
                    .shopId(order.getShop().getId())
                    .shopName(order.getShop().getAddress())
                    .customerName(profile.getFirstName() + " " + profile.getLastName())
                    .mobileNumber(profile.getPhoneNumber())
                    .orderItems(items)
                    .timestamp(DateUtil.getTimestampString(order.getCreatedAt()))
                    .build()
            );
        }
        return response;
    }

    // TODO: Deprecated
    private void updateOrderDetails(OrderCreateDto orderCreateDto) {
        byte deliveryId = orderCreateDto.getDeliveryId();
        byte packagingId = orderCreateDto.getPackagingId();
        if (deliveryId != 0) {
            orderCreateDto.setDeliveryType(
                deliveryId == 1 ? "Home Delivery" : "Store Pickup"
            );
        }

        if (packagingId != 0) {
            orderCreateDto.setPackagingType(
                packagingId == 1 ? "Basic" : "Box"
            );
        }
    }
}
