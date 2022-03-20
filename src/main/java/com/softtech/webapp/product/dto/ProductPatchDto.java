package com.softtech.webapp.product.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductPatchDto {
    private String productName;
    private BigDecimal price;
    private Long productTypeId;
}
