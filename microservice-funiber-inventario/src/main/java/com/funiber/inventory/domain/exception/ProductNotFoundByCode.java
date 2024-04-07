package com.funiber.inventory.domain.exception;

public class ProductNotFoundByCode extends CustomException {

    public ProductNotFoundByCode(String code, String statusCode) {
        super(String.format("No existe el product con el codigo %s", code), statusCode);
    }

    public ProductNotFoundByCode(String code) {
        this(code, "400");
    }

}
