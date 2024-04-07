package com.funiber.inventory.domain.exception;

public class ProductExistException extends CustomException {

    public ProductExistException(String code, String status) {
        super(String.format("Producto ya existe con este code %s", code), status);
    }

    public ProductExistException(String code) {
        this(code, "400");
    }
}
