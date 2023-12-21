package com.example.bhazi.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.bhazi.admin.domain.ShopService;
import com.example.bhazi.admin.domain.model.Shop;
import com.example.bhazi.order.domain.model.DeliveryTimePref;
import com.example.bhazi.profile.domain.model.Address;
import com.example.bhazi.profile.domain.model.Profile;
import com.example.bhazi.profile.dto.AddressCreateDto;
import com.example.bhazi.profile.dto.AddressResponseDto;
import com.example.bhazi.profile.dto.AddressUpdateDto;
import com.example.bhazi.profile.dto.DeliveryTimePrefResponseDto;
import com.example.bhazi.util.Distance;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AddressMapper {

    private final ShopService shopService;

    public Address mapFromDto(AddressCreateDto addressCreateDto, Profile profile) {
        Address address = new Address();
        
        address.setFullAddress(addressCreateDto.getCompleteAddress());
        /* address.setDeliveryTimePref(DeliveryTimePref.valueOf(
            addressCreateDto.getDeliveryTimePref().toUpperCase()
        )); */
        address.setLattitude(addressCreateDto.getLattitude());
        address.setLongitude(addressCreateDto.getLongitude());
        address.setProfile(profile);
        address.setDeliveryTimePref(DeliveryTimePref.ALL_DAY);      // TODO: Deprecated
        populateAddressField(addressCreateDto, address);
        return address;
    }

    // TODO: Deprecated
    private void populateAddressField(AddressCreateDto addressCreateDto, Address address) {
        address.setFloor(addressCreateDto.getFloor() == null ? "" : addressCreateDto.getFloor());
        address.setTag(addressCreateDto.getTag() == null ? "" : addressCreateDto.getTag());
        address.setInstruction(addressCreateDto.getInstruction() == null ? "" : addressCreateDto.getInstruction());
    }

    public Address mapFromDto(AddressUpdateDto addressCreateDto, Address address) {
        address.setFloor(addressCreateDto.getFloor());
        address.setFullAddress(addressCreateDto.getCompleteAddress());
        address.setInstruction(addressCreateDto.getInstruction());
        /* address.setDeliveryTimePref(DeliveryTimePref.valueOf(
            addressCreateDto.getDeliveryTimePref().toUpperCase()
        )); */
        address.setTag(addressCreateDto.getTag());
        address.setLattitude(addressCreateDto.getLattitude());
        address.setLongitude(addressCreateDto.getLongitude());
        return address;
    }

    public AddressResponseDto mapToDto(Address address) {
        if (address == null) {
            return null;
        }
        double distance = calculateDistance(address);
        return AddressResponseDto.builder()
                .id(address.getId())
                .floor(address.getFloor() == null ? "" : address.getFloor())
                .completeAddress(address.getFullAddress())
                .instruction(address.getInstruction())
                .tag(address.getTag())
                .distanceFromShop(distance)
                .lattitude(address.getLattitude())
                .longitude(address.getLongitude())
                .build();
    }

    private double calculateDistance(Address address) {
        double distance = Double.MAX_VALUE;
        double lattitude = address.getLattitude().doubleValue();
        double longitude = address.getLongitude().doubleValue();
        List<Shop> shopList = shopService.getAll();
        for (Shop shop : shopList) {
            double newDistance = Distance.distance(
                lattitude, 
                longitude, 
                shop.getLattitude().doubleValue(),
                shop.getLongitude().doubleValue()
            );
            if (newDistance < distance) {
                distance = newDistance;
            }
        }
        return distance <= 0.0 ? 0.1 : distance;
    }

    public List<AddressResponseDto> mapToDtos(List<Address> addresses) {
        return addresses.stream()
                .map(address -> mapToDto(address))
                .collect(Collectors.toList());
    }

    public AddressResponseDto mapToBasicDto(Address address) {
        return AddressResponseDto.builder()
                .id(address.getId())
                .floor(address.getFloor())
                .completeAddress(address.getFullAddress())
                .build();
    }

    public DeliveryTimePrefResponseDto mapToDto(DeliveryTimePref deliveryTimePref) {
        /* Map<String, String> responseMap = new HashMap<>();
        for (DeliveryTimePref deliveryTimePref : deliveryTimePrefs) {
            responseMap.put(deliveryTimePref.toString(), deliveryTimePref.getDescription());
        } 
        return responseMap; */
        return DeliveryTimePrefResponseDto.builder()
                .key(deliveryTimePref.toString())
                .value(deliveryTimePref.getDescription())
                .build();
    }

    public List<DeliveryTimePrefResponseDto> mapToDtos(DeliveryTimePref[] deliveryTimePrefs) {
        List<DeliveryTimePrefResponseDto> responseDtos = new ArrayList<>();
        for (DeliveryTimePref deliveryTimePref : deliveryTimePrefs) {
            responseDtos.add(mapToDto(deliveryTimePref));
        }
        return responseDtos;
    }
}
