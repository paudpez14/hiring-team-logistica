package com.funiber.inventory.domain.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Inventory {
    private Long id;
    private int stockQuantity;
    private double length;
    private double width;
    private double height;
    private Product product;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserResume createdBy;
    private UserResume updatedBy;
    private String isActive;
}
