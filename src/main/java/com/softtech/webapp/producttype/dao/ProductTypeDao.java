package com.softtech.webapp.producttype.dao;

import com.softtech.webapp.producttype.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTypeDao extends JpaRepository<ProductType, Long> {
    ProductType findByName(String name);
    ProductType findByNameUpper(String name);
}
