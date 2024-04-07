package com.funiber.inventory.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewProduct {
    private String code;
    private String name;
    private Long category_id;
}
