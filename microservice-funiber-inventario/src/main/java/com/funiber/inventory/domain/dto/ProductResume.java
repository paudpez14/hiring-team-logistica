package com.funiber.inventory.domain.dto;

import lombok.Data;

@Data
public class ProductResume {
    private Long id;
    private String code;
    private String name;
    private Category category;
    private Inventory inventory;
}
