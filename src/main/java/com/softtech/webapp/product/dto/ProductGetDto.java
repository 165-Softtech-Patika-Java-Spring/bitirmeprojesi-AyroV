package com.softtech.webapp.product.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductGetDto {
    private String productName;
    private BigDecimal price;
    private BigDecimal priceAfterTax;
    private Long productTypeId;
    private String productType;
}
