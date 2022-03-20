package com.softtech.webapp.product.dao;

import com.softtech.webapp.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductDao extends JpaRepository<Product, Long> {
    Product findByProductName(String name);
    Product findByProductNameUpper(String name);
    List<Product> findAllByPriceAfterTaxBetween(BigDecimal min, BigDecimal max);
    List<Product> findAllByProductTypeId(Long id);
}