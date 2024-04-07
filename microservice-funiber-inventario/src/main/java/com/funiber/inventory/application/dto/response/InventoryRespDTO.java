package com.funiber.inventory.application.dto.response;

import java.time.LocalDateTime;

import lombok.Data;
@Data
public class InventoryRespDTO {
    private Long id;
    private int stockQuantity; 
    private double length;
    private double width;
    private double height;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserRespDTO createdBy;
    private UserRespDTO updatedBy;
    private String isActive;
}
