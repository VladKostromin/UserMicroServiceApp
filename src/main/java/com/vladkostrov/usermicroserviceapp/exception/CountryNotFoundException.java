package com.vladkostrov.usermicroserviceapp.exception;

public class CountryNotFoundException extends ApiException {

    public CountryNotFoundException(String message) {
        super(message, "COUNTRY_NOT_FOUND_ERROR_CODE");
    }
}
