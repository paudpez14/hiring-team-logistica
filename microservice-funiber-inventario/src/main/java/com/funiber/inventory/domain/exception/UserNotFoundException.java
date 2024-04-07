package com.funiber.inventory.domain.exception;

public class UserNotFoundException   extends CustomException {

    public UserNotFoundException(String email, String status) {
        super(String.format("No existe la cuenta con el email %s", email), status);
    }

    public UserNotFoundException(String email) {
        this(email, "400");
    }
}
