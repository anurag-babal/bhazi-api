package com.example.bhazi.order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.example.bhazi.order.domain.OrderService;
import com.example.bhazi.order.domain.model.Order;
import com.example.bhazi.order.dto.OrderCreateDto;
import com.example.bhazi.order.dto.OrderResponseDto;
import com.example.bhazi.order.dto.OrderUpdateDto;
import com.example.bhazi.order.dto.admin.AdminOrderDetailResponse;
import com.example.bhazi.order.dto.admin.TotalOrderQuantityResponseDto;
import com.example.bhazi.profile.domain.AddressService;
import com.example.bhazi.profile.domain.ProfileService;
import com.example.bhazi.profile.domain.model.Address;
import com.example.bhazi.profile.domain.model.Profile;
import com.example.bhazi.util.DateUtil;
import com.example.bhazi.util.ResponseBuilder;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
public class OrderController {
    private final ProfileService profileService;
    private final AddressService addressService;
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping("")
    public ResponseDto save(
        @Valid @RequestBody OrderCreateDto orderCreateDto
    ) {
        Profile profile = profileService.getById(orderCreateDto.getProfileId());
        Address address = addressService.getById(orderCreateDto.getAddressId());
        Order savedOrder = orderService.save(
            orderMapper.mapToDomain(orderCreateDto, profile, address)
        );
        return ResponseBuilder.createResponse(
            orderMapper.mapToResponseDto(savedOrder)
        );
    }

    @PutMapping("/{id}")
    public ResponseDto update(
        @PathVariable(name = "id") long id,
        @Valid @RequestBody OrderUpdateDto orderUpdateDto
    ) {
        Order updatedOrder = orderService.update(
            id,
            orderMapper.mapToDomain(orderUpdateDto)
        );
        // Order updatedOrder = orderService.update(id, orderUpdateDto.getStatus());
        return ResponseBuilder.createResponse(
            orderMapper.mapToResponseDto(updatedOrder)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseDto delete(@PathVariable(name = "id") long id) {
        return ResponseBuilder.createDeleteResponse(orderService.delete(id));
    }

    @GetMapping("/{id}")
    public ResponseDto getById(@PathVariable(name = "id") long id) {
        Order order = orderService.getById(id);
        return ResponseBuilder
            .createResponse(orderMapper.mapToResponseDto(order));
    }

    @GetMapping("/profile/{profileId}")
    public ResponseDto getByProfileId(
        @PathVariable(name = "profileId") int profileId,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest
            .of(
                page, 
                size, 
                Sort.by("id").descending()
            );
        List<OrderResponseDto> orders = orderMapper
            .mapToResponseDtos(
                orderService
                    .getByProfileId(profileId, pageable)
                    .getContent()
            );
        return ResponseBuilder
            .createListResponse(orders.size(), orders);
    }

    @GetMapping("/v1")
    public ResponseDto getAllNew(
        @RequestParam Optional<String> type,
        @RequestParam Optional<String> status,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "100") int size
    ) {
        List<Order> orderList;
        LocalDate today = DateUtil.getDate();
        Pageable pageable = PageRequest
            .of(
                page, size, Sort.by("id").descending()
            );

        if (type.isPresent()) {
            if (type.get().equals("price")) {
                if (status.isPresent()) {
                    orderList = orderService.getAllByDateAndStatus(
                        today, status.get().toLowerCase()
                    );
                    List<AdminOrderDetailResponse> response = orderMapper
                            .adminOrderDetailResponses(orderList);
                    return ResponseBuilder.createListResponse(
                        response.size(), response
                    );
                } else {
                    orderList = orderService.getAllByDate(today);
                    return ResponseBuilder.createResponse(
                        orderMapper.orderCountDto(orderList)
                    );
                }
            } else if (type.get().equals("delivery")) {
                today = today.minusDays(1);
                if (status.isPresent()) {
                    orderList = orderService.getAllByDateAndStatus(
                        today, status.get().toLowerCase()
                    );
                    List<AdminOrderDetailResponse> response = orderMapper
                            .adminOrderDetailResponses(orderList);
                    return ResponseBuilder.createListResponse(
                        response.size(), response
                    );
                } else {
                    orderList = orderService.getAllByDate(today);
                    return ResponseBuilder.createResponse(
                        orderMapper.deliveryOrderCountDto(orderList)
                    );
                }
            } else if (type.get().equals("total")) {
                today = today.minusDays(1);
                orderList = orderService.getAllByDateAndStatus(today, "ordered");
                List<TotalOrderQuantityResponseDto> response = orderMapper.
                        totalOrderQuantityResponseDto(orderList);        
                Map<String, TotalOrderQuantityResponseDto> responseMap = new HashMap<>();
                response.forEach(order -> responseMap.put(order.getSource(), order));
                return ResponseBuilder.createListResponse(responseMap.size(), responseMap);
            }
        }
        orderList = orderService.getAll(pageable).getContent();
        List<OrderResponseDto> responseDtos = orderMapper.mapToResponseDtos(orderList);
        return ResponseBuilder.createListResponse(responseDtos.size(), responseDtos);
    }

    @GetMapping("/status/{status}")
    public ResponseDto getAllByStatus(
        @PathVariable(name = "status") String status
    ) {
        LocalDate yesterday = DateUtil.getDate().minusDays(1);
        List<Order> orders = orderService
                .getAllByDateAndStatus(yesterday, status);

        List<TotalOrderQuantityResponseDto> ordersResponseDtos = orderMapper
                .totalOrderQuantityResponseDto(orders);

        Map<String, TotalOrderQuantityResponseDto> orderResponseDto = new HashMap<>();
        ordersResponseDtos.forEach(order -> 
                orderResponseDto.put(order.getSource(), order)
        );

        return ResponseBuilder.createListResponse(
            orderResponseDto.size(), orderResponseDto
        );
    }

    @GetMapping("/delivery")
    public ResponseDto getAllForDelivery(
        @RequestParam Optional<String> status
    ) {
        List<Order> orders;
        LocalDate yesterday = DateUtil.getDate().minusDays(1);
        if (status.isPresent()) {
            orders = orderService.getAllByDateAndStatus(yesterday, status.get().toLowerCase());
            List<AdminOrderDetailResponse> ordersResponseDtos = orderMapper
                    .adminOrderDetailResponses(orders);
            return ResponseBuilder.createListResponse(
                ordersResponseDtos.size(), ordersResponseDtos
            );
        } else {
            orders = orderService.getAllByDate(yesterday);
            return ResponseBuilder.createResponse(
                orderMapper.deliveryOrderCountDto(orders)
            );
        }
    }

    @GetMapping("/price")
    public ResponseDto getAllForToday(@RequestParam Optional<String> status) {
        List<Order> orders;
        LocalDate today = DateUtil.getDate();
        if (status.isPresent()) {
            orders = orderService.getAllByDateAndStatus(today, status.get().toLowerCase());
            List<AdminOrderDetailResponse> ordersResponseDtos = orderMapper
                    .adminOrderDetailResponses(orders);
            return ResponseBuilder.createListResponse(
                ordersResponseDtos.size(), ordersResponseDtos
            );
        } else {
            orders = orderService.getAllByDate(today);
            return ResponseBuilder.createResponse(
                orderMapper.orderCountDto(orders)
            );
        }
    }

    // TODO: Deprecated
    @GetMapping("")
    public ResponseDto getAllOrders(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "50") int size
    ) {
        Pageable pageable = PageRequest
            .of(
                page, size, Sort.by("id").descending()
            );
        List<OrderResponseDto> responseDtos = orderMapper
            .mapToResponseDtos(orderService.getAll(pageable).getContent());
        return ResponseBuilder
            .createListResponse(responseDtos.size(), responseDtos);
    }
}
