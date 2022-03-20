package com.softtech.webapp.product.entity;

import com.softtech.webapp.general.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Product extends BaseEntity {
    @Id
    @SequenceGenerator(name = "ProductGen", sequenceName = "PRODUCT_ID_SEQ")
    @GeneratedValue(generator = "ProductGen")
    private Long id;
    private String productName;
    private String productNameUpper;
    private BigDecimal price;
    private BigDecimal priceAfterTax;
    private Long productTypeId;
    private String productType;
}