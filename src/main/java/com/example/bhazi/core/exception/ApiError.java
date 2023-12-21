package com.example.bhazi.core.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.example.bhazi.util.DateUtil;

import lombok.Data;

@Data
public class ApiError {
    private HttpStatus status;
    private String timestamp;
    private String message;
    private String debugMessage;

    ApiError() {
        timestamp = DateUtil.formatDate(new Date());
    }

    ApiError(HttpStatus status) {
        this();
        this.status = status;
        this.message = "Unexpected Error";
    }

    ApiError(HttpStatus status, String message, Throwable ex) {
        this(status);
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }
}
