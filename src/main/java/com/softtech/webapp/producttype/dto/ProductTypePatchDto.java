package com.softtech.webapp.producttype.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProductTypePatchDto {
    @NotNull(message = "Tax % cannot be null")
    private Integer taxPercentage;
}
