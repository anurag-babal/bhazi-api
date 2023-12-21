package com.example.bhazi.core.exception;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.bhazi.core.dto.ResponseDto;
import com.example.bhazi.util.ResponseBuilder;

@ControllerAdvice
@ResponseBody
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @Autowired
    MessageSource messageSource;
    
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseDto handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseBuilder.createExceptionResponse(ex.getLocalizedMessage());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
        NoHandlerFoundException ex, 
        HttpHeaders headers,
        HttpStatus status, 
        WebRequest request
    ) {
        String message = String.format("Could not find %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL());
        ApiError apiError = new ApiError(status, message, ex);
        return buildResponseEntity(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, 
        HttpHeaders headers, 
        HttpStatus status, 
        WebRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach( error -> {
            String fieldName =  error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(GlobalException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseDto handleGlobalException(GlobalException ex) {
        String message = ex.getLocalizedMessage();
        return ResponseBuilder.createExceptionResponse(message);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    protected ResponseDto handleDataIntegrityViolatoin(DataIntegrityViolationException ex) {
        return ResponseBuilder.createExceptionResponse(ex.getMessage()); 
    }

    @ExceptionHandler(ParseException.class)
    protected ResponseDto handleParse(ParseException pe) {
        return ResponseBuilder.createExceptionResponse("Timestamp not in format: dd/MM/yyyy HH:mm:ss");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseDto handleAnyException(Exception ex) {
        return ResponseBuilder.createExceptionResponse(ex.getMessage());
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
