package com.funiber.inventory.application.dto.request;

import lombok.Data;

@Data
public class UpdateInventoryDTO {
    public Long idInventory;
    private int stockQuantity;
    private double length;
    private double width;
    private double height;
    private String createdBy;
}
