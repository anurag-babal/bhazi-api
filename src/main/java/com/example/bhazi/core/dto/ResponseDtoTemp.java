package com.example.bhazi.core.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDtoTemp extends RepresentationModel<ResponseDtoTemp> {
    private int status;
    private int count;
    private Object data;
    private String message;
}
