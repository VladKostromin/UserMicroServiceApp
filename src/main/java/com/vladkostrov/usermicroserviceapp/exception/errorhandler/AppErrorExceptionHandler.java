package com.vladkostrov.usermicroserviceapp.exception.errorhandler;


import com.vladkostrov.usermicroserviceapp.exception.AddressNotFoundException;
import com.vladkostrov.usermicroserviceapp.exception.CountryNotFoundException;
import com.vladkostrov.usermicroserviceapp.exception.IndividualNotFoundException;
import com.vladkostrov.usermicroserviceapp.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class AppErrorExceptionHandler {
    private static  HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAddressNotFoundException(AddressNotFoundException e) {
        status = HttpStatus.NOT_FOUND;
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", e.getErrorCode());
        body.put("message", e.getMessage());
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(CountryNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCountryNotFoundException(CountryNotFoundException e) {
        status = HttpStatus.NOT_FOUND;
        if(e.getMessage().contains("wrong alpha code")) {
            status = HttpStatus.BAD_REQUEST;
        }
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", e.getErrorCode());
        body.put("message", e.getMessage());
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(IndividualNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleIndividualNotFoundException(IndividualNotFoundException e) {
        status = HttpStatus.NOT_FOUND;
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", e.getErrorCode());
        body.put("message", e.getMessage());
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException e) {
        status = HttpStatus.NOT_FOUND;
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", e.getErrorCode());
        body.put("message", e.getMessage());
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        status = HttpStatus.BAD_REQUEST;
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", status.getReasonPhrase());
        body.put("message", e.getMessage());
        return ResponseEntity.status(status).body(body);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleInternalError(Exception e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", status.getReasonPhrase());
        body.put("message", e.getMessage());
        return ResponseEntity.status(status).body(body);
    }
}
