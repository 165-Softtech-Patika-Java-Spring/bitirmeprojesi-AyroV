package com.softtech.webapp.producttype.service;

import com.softtech.webapp.general.service.BaseEntityService;
import com.softtech.webapp.producttype.dao.ProductTypeDao;
import com.softtech.webapp.producttype.entity.ProductType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductTypeEntityService extends BaseEntityService<ProductType, ProductTypeDao> {
    public ProductTypeEntityService (ProductTypeDao dao) {
        super(dao, ProductType.class);
    }

    public ProductType findByName(String name) {
        return getDao().findByName(name);
    }

    public ProductType findByNameUpper(String name) {
        return getDao().findByNameUpper(name);
    }
}
