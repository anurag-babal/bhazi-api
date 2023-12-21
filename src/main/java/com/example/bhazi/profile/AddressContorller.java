package com.example.bhazi.profile;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.example.bhazi.order.domain.model.DeliveryTimePref;
import com.example.bhazi.profile.domain.AddressService;
import com.example.bhazi.profile.domain.ProfileService;
import com.example.bhazi.profile.domain.model.Address;
import com.example.bhazi.profile.domain.model.Profile;
import com.example.bhazi.profile.dto.AddressCreateDto;
import com.example.bhazi.profile.dto.AddressResponseDto;
import com.example.bhazi.profile.dto.AddressUpdateDto;
import com.example.bhazi.profile.dto.DeliveryTimePrefResponseDto;
import com.example.bhazi.util.ResponseBuilder;

import lombok.RequiredArgsConstructor;

@RestController()
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressContorller {
    private final AddressService addressService;
    private final ProfileService profileService;
    private final AddressMapper addressMapper;
    private static final int ADDRESS_PAGE_SIZE = 20;

    @PostMapping("")
    public ResponseDto save(
        @Valid @RequestBody AddressCreateDto addressCreateDto
    ) {
        Profile profile = profileService.getById(addressCreateDto.getProfileId());
        Address savedAddress = addressService.save(
            addressMapper.mapFromDto(addressCreateDto, profile)
        );
        return ResponseBuilder.createResponse(
            addressMapper.mapToDto(savedAddress)
        );
    }

    @PutMapping("/{id}")
    public ResponseDto update(
            @PathVariable(name = "id") long id,
            @Valid @RequestBody AddressUpdateDto addressUpdateDto
    ) {
        Address address = addressMapper.mapFromDto(
            addressUpdateDto, addressService.getById(id)
        );
        Address updatedAddress = addressService.update(id, address);
        return ResponseBuilder.createResponse(
            addressMapper.mapToDto(updatedAddress)
        );
    }

    @PutMapping("/status/{id}")
    public ResponseDto updateActive(
            @PathVariable(name = "id") long id,
            @RequestParam(name = "active") boolean isActive
    ) {
        Address updatedAddress = addressService.updateActive(id, isActive);
        return ResponseBuilder.createDeleteResponse(!updatedAddress.isActive());
    }

    @DeleteMapping("/{id}")
    public ResponseDto delete(@PathVariable(name = "id") long id) {
        return ResponseBuilder.createDeleteResponse(addressService.delete(id));
    }

    @GetMapping("/{id}")
    public ResponseDto getById(@PathVariable(name = "id") long id) {
        Address address = addressService.getById(id);
        AddressResponseDto responseDto = addressMapper.mapToDto(address);
        return ResponseBuilder.createResponse(responseDto);
    }
    
    @GetMapping("")
    public ResponseDto getAll(
        @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        Pageable pageable = PageRequest.of(page, ADDRESS_PAGE_SIZE);
        List<Address> addresses = addressService.getAll(pageable);
        List<AddressResponseDto> responseDtos = addressMapper.mapToDtos(addresses);
        return ResponseBuilder.createListResponse(responseDtos.size(), responseDtos);
    }

    @GetMapping("/profile/{profileId}")
    public ResponseDto getByProfileId(
        @PathVariable(name = "profileId") int profileId,
        @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        Pageable pageable = PageRequest.of(page, ADDRESS_PAGE_SIZE);
        List<Address> addresses = addressService.getByProfileId(profileId, pageable).getContent();
        List<AddressResponseDto> responseDto = addressMapper.mapToDtos(addresses);
        return ResponseBuilder.createListResponse(responseDto.size(), responseDto);
    }

    @GetMapping("/deliveryTimePref")
    public ResponseDto getDeliveryTimePrefs() {
        List<DeliveryTimePrefResponseDto> responseDtos = addressMapper
                .mapToDtos(DeliveryTimePref.values());
        return ResponseBuilder.createListResponse(responseDtos.size(), responseDtos);
    }
}
