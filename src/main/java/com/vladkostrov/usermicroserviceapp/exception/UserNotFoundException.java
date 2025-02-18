package com.vladkostrov.usermicroserviceapp.exception;

public class UserNotFoundException extends ApiException {

    public UserNotFoundException(String message) {
        super(message, "USER_NOT_FOUND_ERROR_CODE");
    }
}
