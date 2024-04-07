package com.funiber.inventory.application.dto.response;

import lombok.Data;

@Data
public class ProductResumeRespDTO {
    private Long id;
    private String code;
    private String name;
    private CategoryRespDTO category;
    private InventoryRespDTO inventory;
}
