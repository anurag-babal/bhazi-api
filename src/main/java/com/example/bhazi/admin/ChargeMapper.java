package com.example.bhazi.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.bhazi.admin.domain.model.Charge;
import com.example.bhazi.admin.dto.ChargeCreateDto;
import com.example.bhazi.admin.dto.ChargeResponseDto;
import com.example.bhazi.admin.dto.ChargeUpdateDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChargeMapper {

    public Charge mapFromDto(ChargeCreateDto chargeCreateDto) {
        Charge charge = new Charge();
        charge.setTransport(chargeCreateDto.getTransport());
        charge.setDelivery(chargeCreateDto.getDelivery());
        charge.setNetwork(chargeCreateDto.getNetwork());
        charge.setPlatform(chargeCreateDto.getPlatform());
        charge.setApi(chargeCreateDto.getApi());
        charge.setHosting(chargeCreateDto.getHosting());
        charge.setOperation(chargeCreateDto.getOperation());
        charge.setHandling(chargeCreateDto.getHandling());
        charge.setProfit(chargeCreateDto.getProfit());
        charge.setSlippage(chargeCreateDto.getSlippage());
        return charge;
    }

    public Charge mapFromDto(ChargeUpdateDto chargeUpdateDto, Charge charge) {
        charge.setTransport(chargeUpdateDto.getTransport());
        charge.setDelivery(chargeUpdateDto.getDelivery());
        charge.setNetwork(chargeUpdateDto.getNetwork());
        charge.setPlatform(chargeUpdateDto.getPlatform());
        charge.setApi(chargeUpdateDto.getApi());
        charge.setHosting(chargeUpdateDto.getHosting());
        charge.setOperation(chargeUpdateDto.getOperation());
        charge.setHandling(chargeUpdateDto.getHandling());
        charge.setProfit(chargeUpdateDto.getProfit());
        charge.setSlippage(chargeUpdateDto.getSlippage());
        return charge;
    }

    public ChargeResponseDto mapToDto(Charge charge) {
        return ChargeResponseDto.builder()
                .id(charge.getId())
                .transport(charge.getTransport())
                .delivery(charge.getDelivery())
                .network(charge.getNetwork())
                .platform(charge.getPlatform())
                .api(charge.getApi())
                .hosting(charge.getHosting())
                .operation(charge.getOperation())
                .handling(charge.getHandling())
                .profit(charge.getProfit())
                .slippage(charge.getSlippage())
                .build();
    }

    public List<ChargeResponseDto> mapToDtos(List<Charge> charges) {
        return charges.stream()
                .map(charge -> mapToDto(charge))
                .collect(Collectors.toList());
    }
}
