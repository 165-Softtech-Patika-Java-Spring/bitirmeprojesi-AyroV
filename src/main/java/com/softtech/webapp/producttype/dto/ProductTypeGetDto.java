package com.softtech.webapp.producttype.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductTypeGetDto {
    private String name;
    private Integer taxPercentage;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private BigDecimal avgPrice;
    private Integer productCount;
}
