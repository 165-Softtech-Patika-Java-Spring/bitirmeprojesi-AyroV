package com.softtech.webapp.producttype.entity;

import com.softtech.webapp.general.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class ProductType extends BaseEntity {
    @Id
    @SequenceGenerator(name = "TypeGen", sequenceName = "PRODUCT_TYPE_ID_SEQ")
    @GeneratedValue(generator = "TypeGen")
    private Long id;
    private String name;
    private String nameUpper;
    private Integer taxPercentage;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private BigDecimal avgPrice;
    private Integer productCount;
}
