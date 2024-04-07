package com.funiber.inventory.domain.dto;

import lombok.Data;

@Data
public class Product {
    private Long id;
    private String code;
    private String name;
    private Category category;
}
