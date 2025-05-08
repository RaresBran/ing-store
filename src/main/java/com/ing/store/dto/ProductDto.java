package com.ing.store.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String id;
    private String name;
    private Double price;
    private Integer stock;
}
