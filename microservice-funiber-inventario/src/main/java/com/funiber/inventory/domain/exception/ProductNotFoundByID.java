package com.funiber.inventory.domain.exception;

public class ProductNotFoundByID extends CustomException {

    public ProductNotFoundByID(Long code, String statusCode) {
        super(String.format("No existe el product con el codigo %s", code), statusCode);
    }

    public ProductNotFoundByID(Long code) {
        this(code, "400");
    }

}
