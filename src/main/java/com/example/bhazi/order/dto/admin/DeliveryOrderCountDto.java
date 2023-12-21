package com.example.bhazi.order.dto.admin;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class DeliveryOrderCountDto {
    Map<String, Integer> delivery;
    Map<String, Float> price;
    Map<String, Map<String, Integer>> tmp;
}
