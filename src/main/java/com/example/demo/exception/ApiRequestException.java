package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class ApiRequestException extends RuntimeException {

    public ApiRequestException(String message) {
        super(message);
    }

}
