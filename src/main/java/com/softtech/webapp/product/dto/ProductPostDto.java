package com.softtech.webapp.product.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ProductPostDto {
    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    private String productName;
    @NotNull(message = "Price cannot be null")
    private BigDecimal price;
    @NotNull(message = "Type Id cannot be null")
    private Long productTypeId;
}
