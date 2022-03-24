package com.softtech.webapp.producttype.service;

import com.softtech.webapp.general.enums.ErrorMessage;
import com.softtech.webapp.general.exceptions.BusinessException;
import com.softtech.webapp.general.exceptions.ItemNotFoundException;
import com.softtech.webapp.product.entity.Product;
import com.softtech.webapp.product.service.ProductEntityService;
import com.softtech.webapp.producttype.dto.ProductTypeGetDto;
import com.softtech.webapp.producttype.dto.ProductTypePatchDto;
import com.softtech.webapp.producttype.dto.ProductTypePostDto;
import com.softtech.webapp.producttype.entity.ProductType;
import com.softtech.webapp.producttype.enums.ProductTypeErrorMessage;
import com.softtech.webapp.user.dto.UserGetDto;
import com.softtech.webapp.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductTypeService {
    private final ProductTypeEntityService productTypeEntityService;
    private final ProductEntityService productEntityService;
    private final ModelMapper mapper;

    public List<ProductTypeGetDto> findAll() {
        List<ProductType> productTypeList = productTypeEntityService.findAll();
        List<ProductTypeGetDto> productTypeGetDtoList = productTypeList.stream().map(productType -> mapper.map(productType, ProductTypeGetDto.class)).collect(Collectors.toList());
        return productTypeGetDtoList;
    }

    public ProductTypeGetDto findById(Long id) {
        ProductType productType = productTypeEntityService.getByIdWithControl(id);
        return mapper.map(productType, ProductTypeGetDto.class);
    }

    public ProductTypeGetDto findByName(String name) {
        ProductType productType = productTypeEntityService.findByName(name);
        validateProductType(productType);
        return mapper.map(productType, ProductTypeGetDto.class);
    }

    public ProductTypeGetDto save(ProductTypePostDto productTypePostDto) {
        isNameUnique(productTypePostDto.getName().toUpperCase());
        validateTax(productTypePostDto.getTaxPercentage());

        ProductType productType = mapper.map(productTypePostDto, ProductType.class);
        productType.setProductCount(0);
        productType.setAvgPrice(BigDecimal.ZERO);
        productType.setMaxPrice(BigDecimal.ZERO);
        productType.setMinPrice(BigDecimal.ZERO);

        productType = productTypeEntityService.save(productType, false);

        return mapper.map(productType, ProductTypeGetDto.class);
    }

    public ProductTypeGetDto update(ProductTypePatchDto productTypePatchDto, Long id) {
        validateTax(productTypePatchDto.getTaxPercentage());

        ProductType productType = productTypeEntityService.getByIdWithControl(id);

        productType.setTaxPercentage(productTypePatchDto.getTaxPercentage());
        productType.setMinPrice(BigDecimal.ZERO);

        List<Product> productList = productEntityService.findAllByProductTypeId(id);

        BigDecimal sumPrice = BigDecimal.ZERO;
        BigDecimal avgPrice;

        for(Product product : productList) {
            BigDecimal newPrice = product.getPrice().add(product.getPrice().divide(BigDecimal.valueOf(100.0)).multiply(BigDecimal.valueOf(productType.getTaxPercentage())));
            product.setPriceAfterTax(newPrice);
            productEntityService.save(product, true);

            if(productType.getMaxPrice().compareTo(newPrice) < 0)
                productType.setMaxPrice(newPrice);
            if(productType.getMinPrice().compareTo(newPrice) > 0 || productType.getMinPrice().equals(BigDecimal.ZERO))
                productType.setMinPrice(newPrice);

            sumPrice = sumPrice.add(newPrice);
        }

        avgPrice = sumPrice.divide(BigDecimal.valueOf(productType.getProductCount()));
        productType.setAvgPrice(avgPrice);

        productType = productTypeEntityService.save(productType, true);
        return mapper.map(productType, ProductTypeGetDto.class);
    }

    private void validateProductType(ProductType productType) {
        if(productType == null)
            throw new ItemNotFoundException(ErrorMessage.ITEM_NOT_FOUND, ProductType.class.getSimpleName());
    }

    private void isNameUnique(String name) {
        ProductType productType = productTypeEntityService.findByNameUpper(name);
        if(productType != null)
            throw new BusinessException(ProductTypeErrorMessage.NAME_ALREADY_TAKEN);
    }

    private void validateTax(Integer tax) {
        if(tax.compareTo(0) < 0) {
            throw new BusinessException(ProductTypeErrorMessage.TAX_BELOW_ZERO);
        }
    }
}
