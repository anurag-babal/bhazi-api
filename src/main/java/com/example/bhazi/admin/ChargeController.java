package com.example.bhazi.admin;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bhazi.admin.domain.ChargeService;
import com.example.bhazi.admin.domain.model.Charge;
import com.example.bhazi.admin.dto.ChargeCreateDto;
import com.example.bhazi.admin.dto.ChargeResponseDto;
import com.example.bhazi.admin.dto.ChargeUpdateDto;
import com.example.bhazi.core.dto.ResponseDto;
import com.example.bhazi.util.ResponseBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/charges")
@RequiredArgsConstructor
public class ChargeController {
    private final ChargeService chargeService;
    private final ChargeMapper chargeMapper;

    @PostMapping("")
    public ResponseDto saveCharges(@RequestBody ChargeCreateDto chargeCreateDto) {
        Charge savedCharge = chargeService.save(
            chargeMapper.mapFromDto(chargeCreateDto)
        );
        ChargeResponseDto responseDto = chargeMapper.mapToDto(savedCharge);
        return ResponseBuilder.createResponse(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseDto updateCharges(
        @PathVariable(value = "id") int id,
        @RequestBody ChargeUpdateDto chargeUpdateDto
    ) {
        Charge oldCharge = chargeService.getById(id);
        Charge updatedCharge = chargeService.save(
            chargeMapper.mapFromDto(chargeUpdateDto, oldCharge)
        );
        ChargeResponseDto responseDto = chargeMapper.mapToDto(updatedCharge);
        return ResponseBuilder.createResponse(responseDto);
    }

    @GetMapping("")
    public ResponseDto getAll() {
        List<Charge> charges = chargeService.getAll();
        List<ChargeResponseDto> responseDtos = chargeMapper.mapToDtos(charges);
        return ResponseBuilder.createListResponse(responseDtos.size(), responseDtos);
    }

    @GetMapping("/{id}")
    public ResponseDto getById(@PathVariable(value = "id") int id) {
        Charge charge = chargeService.getById(id);
        ChargeResponseDto responseDto = chargeMapper.mapToDto(charge);
        return ResponseBuilder.createResponse(responseDto);
    }

    @GetMapping("/latest")
    public ResponseDto getLatest() {
        Charge charge = chargeService.getLatest();
        ChargeResponseDto responseDto = chargeMapper.mapToDto(charge);
        return ResponseBuilder.createResponse(responseDto);
    }
}
