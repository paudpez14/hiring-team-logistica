package com.funiber.inventory.domain.exception;

public class CategoryNotFoundByCode extends CustomException {

    public CategoryNotFoundByCode(String code, String statusCode) {
        super(String.format("No existe la categoria con el codigo %s", code), statusCode);
    }

    public CategoryNotFoundByCode(String code) {
        this(code, "400");
    }

}
