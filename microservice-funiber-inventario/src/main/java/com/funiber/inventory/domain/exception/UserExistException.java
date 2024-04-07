package com.funiber.inventory.domain.exception;

public class UserExistException extends CustomException {

    public UserExistException(String email, String status) {
        super(String.format("Cuenta ya existe para el cliente con el email %s", email), status);
    }

    public UserExistException(String email) {
        this(email, "400");
    }
}
