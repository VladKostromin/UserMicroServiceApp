package com.vladkostrov.usermicroserviceapp.exception;

public class AddressNotFoundException extends ApiException {
    public AddressNotFoundException(String message) {
        super(message, "ADDRESS_NOT_FOUND_ERROR_CODE");
    }
}
