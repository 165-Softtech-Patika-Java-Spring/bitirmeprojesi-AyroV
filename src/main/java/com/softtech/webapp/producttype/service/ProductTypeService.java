package com.softtech.webapp.producttype.service;

import com.softtech.webapp.general.enums.ErrorMessage;
import com.softtech.webapp.general.exceptions.BusinessException;
import com.softtech.webapp.general.exceptions.ItemNotFoundException;
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
        productType.setAvgPrice(BigDecimal.valueOf(0.0));
        productType.setMaxPrice(BigDecimal.valueOf(0.0));
        productType.setMinPrice(BigDecimal.valueOf(0.0));

        productType = productTypeEntityService.save(productType, false);

        return mapper.map(productType, ProductTypeGetDto.class);
    }

    public ProductTypeGetDto update(ProductTypePatchDto productTypePatchDto, Long id) {
        validateTax(productTypePatchDto.getTaxPercentage());

        ProductType productType = productTypeEntityService.getByIdWithControl(id);

        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.map(productTypePatchDto, productType);

        //Update all product prices according to new tax

        //Update max-min-avg prices according to new prices

        productType = productTypeEntityService.save(productType, true);
        return mapper.map(productType, ProductTypeGetDto.class);
    }

    private void validateProductType(ProductType productType) {
        if(productType == null)
            throw new ItemNotFoundException(ErrorMessage.ITEM_NOT_FOUND);
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
