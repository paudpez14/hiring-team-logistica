package com.funiber.inventory.domain.dto;

import lombok.Data;

@Data
public class NewInventory {
    public String idProduct;
    private int stockQuantity;
    private double length;
    private double width;
    private double height;
    private String createdBy;
}
