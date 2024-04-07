package com.funiber.inventory.domain.dto;
import lombok.Data;

@Data
public class UpdateProduct {
    private String code;
    private String name;
    private Long category_id;
}
