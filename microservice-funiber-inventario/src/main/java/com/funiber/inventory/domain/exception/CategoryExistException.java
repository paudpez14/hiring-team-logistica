package com.funiber.inventory.domain.exception;

public class CategoryExistException extends CustomException {

    public CategoryExistException(String email, String status) {
        super(String.format("Categoria ya existe para el code %s", email), status);
    }

    public CategoryExistException(String email) {
        this(email, "400");
    }
}
