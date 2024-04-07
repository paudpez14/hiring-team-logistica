package com.funiber.inventory.application.dto.response;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageDTO<T> {
    private Integer current;
    private boolean hasNext;
    private boolean hasPrevious;
    private int numPages;
    private Long sizeData;
    private Collection<T> results;
}