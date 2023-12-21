package com.example.bhazi.core.exception;

public class EntityNotFoundException extends GlobalException {

    public EntityNotFoundException() {
        super("exception.user.not.found");
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
