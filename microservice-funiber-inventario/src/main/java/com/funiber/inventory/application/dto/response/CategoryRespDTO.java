package com.funiber.inventory.application.dto.response;

import lombok.Data;

@Data
public class CategoryRespDTO {
    private Long id;
    private String name;
    private String code;
    private String isActive;
}
