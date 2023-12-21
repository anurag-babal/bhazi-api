package com.example.bhazi.util;

import com.example.bhazi.core.dto.ResponseDto;

public class ResponseBuilder {
    public static ResponseDto createResponse(Object obj) {
        if (obj == null) 
            return createNullResponse();
        else
            return createSinglResponse(obj);
    }

    public static ResponseDto createDeleteResponse(boolean successfull) {
        return createDeleteSuccessfullResponse();
    }

    public static ResponseDto createNullResponse() {
        return new ResponseDto.Builder()
                .setStatus(404)
                .setCount(0)
                .setMessage("Item not found")
                .build();
    }

    public static ResponseDto createSinglResponse(Object obj) {
        return new ResponseDto.Builder()
                .setStatus(200)
                .setCount(1)
                .setData(obj)
                .setMessage("Successfull")
                .build();
    }

    public static ResponseDto createDeleteSuccessfullResponse() {
        return new ResponseDto.Builder()
                .setStatus(200)
                .setCount(0)
                .setMessage("Deleted successfully")
                .build();
    }

    public static ResponseDto createListResponse(int count, Object obj) {
        return new ResponseDto.Builder()
                .setStatus(200)
                .setCount(count)
                .setData(obj)
                .setMessage("Successfull")
                .build();
    }

    public static ResponseDto createExceptionResponse(int status, String message) {
        return new ResponseDto.Builder()
                .setStatus(status)
                .setCount(0)
                .setData(null)
                .setMessage(message)
                .build();
    }

    public static ResponseDto createExceptionResponse(String message) {
        return new ResponseDto.Builder()
                .setStatus(400)
                .setCount(0)
                .setData(null)
                .setMessage(message)
                .build();
    }
}
