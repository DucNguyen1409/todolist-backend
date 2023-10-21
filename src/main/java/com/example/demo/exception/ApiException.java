package com.example.demo.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ApiException(String msg,
                            HttpStatus httpStatus,
                            ZonedDateTime timeStamp) {
}
