package com.funiber.inventory.domain.exception;

import lombok.Data;


@Data
public class CustomException extends RuntimeException {
    private String statusCode = "400";
    public CustomException(String message, String statusCode){
        super(message);
        this.statusCode = statusCode;
    }
}