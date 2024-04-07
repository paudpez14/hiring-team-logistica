package com.funiber.inventory.application.dto.response;

import java.util.Collection;

import lombok.Data;

@Data
public class ProductDetailRespDTO {
    public ProductResumeRespDTO product;
    public Collection<InventoryRespDTO> history;
}
