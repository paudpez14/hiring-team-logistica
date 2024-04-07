package com.funiber.inventory.application.dto.request;

import lombok.Data;

@Data
public class NewInventoryDTO {
    public Long idProduct;
    private int stockQuantity;
    private double length;
    private double width;
    private double height;
    private String createdBy;
}
