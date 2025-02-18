package com.vladkostrov.usermicroserviceapp.exception;

public class IndividualNotFoundException extends ApiException {
    public IndividualNotFoundException(String message) {
        super(message, "INDIVIDUAL_NOT_FOUND_ERROR_CODE");
    }
}
