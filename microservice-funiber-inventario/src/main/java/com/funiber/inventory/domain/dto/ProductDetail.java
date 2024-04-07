package com.funiber.inventory.domain.dto;

import java.util.Collection;

import lombok.Data;

@Data
public class ProductDetail {
    public ProductResume product;
    public Collection<Inventory> history;
}
