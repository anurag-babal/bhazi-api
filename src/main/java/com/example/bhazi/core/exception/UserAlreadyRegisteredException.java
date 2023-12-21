package com.example.bhazi.core.exception;

public class UserAlreadyRegisteredException extends GlobalException {
    public UserAlreadyRegisteredException() {
        super("exception.user.already.registered");
    }

    public UserAlreadyRegisteredException(String message) {
        super(message);
    }
}
