package com.softtech.webapp.product.service;

import com.softtech.webapp.general.service.BaseEntityService;
import com.softtech.webapp.product.dao.ProductDao;
import com.softtech.webapp.product.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ProductEntityService extends BaseEntityService<Product, ProductDao> {
    public ProductEntityService (ProductDao dao) {
        super(dao, Product.class);
    }

    public Product findByProductName(String name) {
        return getDao().findByProductName(name);
    }

    public Product findByProductNameUpper(String name){
        return getDao().findByProductNameUpper(name);
    }

    public List<Product> findAllByPriceAfterTaxBetween(BigDecimal min, BigDecimal max){
        return getDao().findAllByPriceAfterTaxBetween(min, max);
    }

    public List<Product> findAllByProductTypeId(Long id){
        return getDao().findAllByProductTypeId(id);
    }
}
