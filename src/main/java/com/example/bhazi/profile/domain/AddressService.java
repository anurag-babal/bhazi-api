package com.example.bhazi.profile.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bhazi.admin.domain.ShopService;
import com.example.bhazi.admin.domain.model.Shop;
import com.example.bhazi.core.exception.EntityNotFoundException;
import com.example.bhazi.core.exception.GlobalException;
import com.example.bhazi.order.domain.OrderService;
import com.example.bhazi.order.domain.model.Order;
import com.example.bhazi.profile.domain.model.Address;
import com.example.bhazi.util.Distance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressService {
    private final AddressRepository addressRepository;
    private final ShopService shopService;
    private final OrderService orderService;

    public static final double DELIVERABLE_DISTANCE = 10.0;

    @Transactional(noRollbackFor = GlobalException.class)
    public Address save(Address address) {
        Address savedAddress;
        if (checkForDeliveryEligibility(address)) {
            address.setActive(true);
            savedAddress = addressRepository.save(address);
        } else {
            address.setActive(false);
            addressRepository.save(address);
            throw new GlobalException("No delivery to this location");
        }
        return savedAddress;
    }

    @Transactional
    public Address update(long id, Address address) {
        Address updatedAddress;
        if (checkForDeliveryEligibility(address)) {
            updatedAddress = addressRepository.save(address);
        } else {
            throw new GlobalException("No delivery to this location");
        }
        return updatedAddress;
    }

    private boolean checkForDeliveryEligibility(Address address) {
        double lattitude = address.getLattitude().doubleValue();
        double longitude = address.getLongitude().doubleValue();
        List<Shop> shopList = shopService.getAll();
        for (Shop shop : shopList) {
            double distance = Distance.distance(
                    lattitude, 
                    longitude, 
                    shop.getLattitude().doubleValue(),
                    shop.getLongitude().doubleValue()
                );
            if (distance < DELIVERABLE_DISTANCE)
                return true;
        }
        log.info(
            "Distance is not in range for profile Id: {} with latitude: {} and longitude: {}", 
            address.getProfile().getId(),
            address.getLattitude(),
            address.getLongitude()
        );
        return false;
    }

    @Transactional
    public Address updateActive(long id, boolean isActive) {
        Address address = getById(id);
        address.setActive(isActive);
        return addressRepository.save(address);
    }

    public boolean delete(long id) {
        Address address = getById(id);
        addressRepository.delete(address);
        return true;
    }

    public Address getById(long id) {
        Address address = addressRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Address not present with id = " + id)
        );
        return address;
    }

    public Page<Address> getByProfileId(int profileId, Pageable pageable) {
        Page<Address> addresses = addressRepository
                .findByProfileIdAndIsActive(profileId, true, pageable);
        return addresses;
    }

    public List<Address> getAll(Pageable pageable) {
        List<Address> addresses = addressRepository.findAll(pageable).getContent();
        return addresses;
    }
    
	public Optional<Address> getDefaultAddressForProfileId(int profileId) {
		Order order = orderService.getLatestOrderForProfileId(profileId);
		Address returnAddress = null;
		if (order != null) {
			returnAddress = order.getAddress();
		} else {
			Optional<Address> address = addressRepository.findTopByProfileIdAndIsActive(profileId, true);
			if (address.isPresent()) {
				returnAddress = address.get();
			}
		}
		return Optional.ofNullable(returnAddress);
	}
    
    public double calculateDistance(long addressId) {
    	Address address = getById(addressId);
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
}
