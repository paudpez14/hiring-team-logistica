package com.funiber.inventory.domain.dto;


import lombok.Data;

@Data
public class Category {
    private Long id;
    private String name;
    private String code;
    private String isActive;

}
