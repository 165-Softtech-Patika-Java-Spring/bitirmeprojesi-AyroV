package com.softtech.webapp.product.service;

import com.softtech.webapp.general.enums.ErrorMessage;
import com.softtech.webapp.general.exceptions.BusinessException;
import com.softtech.webapp.general.exceptions.ItemNotFoundException;
import com.softtech.webapp.product.dto.*;
import com.softtech.webapp.product.entity.Product;
import com.softtech.webapp.product.enums.ProductErrorMessage;

import com.softtech.webapp.producttype.entity.ProductType;
import com.softtech.webapp.producttype.service.ProductTypeEntityService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductEntityService productEntityService;
    private final ProductTypeEntityService productTypeEntityService;
    private final ModelMapper mapper;

    public List<ProductGetDto> findAll(Optional<BigDecimal> minPrice, Optional<BigDecimal> maxPrice, Optional<String> productTypeName) {
        List<Product> productList = null;
        if (minPrice.isPresent() && maxPrice.isPresent()) {
            productList = productEntityService.findAllByPriceAfterTaxBetween(minPrice.get(), maxPrice.get());
        }

        if (productTypeName.isPresent()) {
            ProductType productType = productTypeEntityService.findByName(productTypeName.get());
            Long productTypeId = -1L;
            if(productType == null)
                productEntityService.getByIdWithControl(productTypeId);
            else
                productTypeId = productType.getId();
            if (productList == null)
                productList = productEntityService.findAllByProductTypeId(productTypeId);
            else {
                Long finalProductTypeId = productTypeId;
                productList = productList.stream().filter(product -> Objects.equals(product.getProductTypeId(), finalProductTypeId)).collect(Collectors.toList());
            }
        }

        if (productList == null)
            productList = productEntityService.findAll();

        List<ProductGetDto> productGetDtoList = productList.stream().map(product -> mapper.map(product, ProductGetDto.class)).collect(Collectors.toList());
        return productGetDtoList;
    }

    public ProductGetDto findById(Long id) {
        Product product = productEntityService.getByIdWithControl(id);
        return mapper.map(product, ProductGetDto.class);
    }

    public ProductGetDto findByName(String name) {
        Product product = productEntityService.findByProductName(name);
        validateProduct(product);
        return mapper.map(product, ProductGetDto.class);
    }

    public ProductGetDto save(ProductPostDto productPostDto) {
        isNameUnique(productPostDto.getProductName().toUpperCase());
        validatePrice(productPostDto.getPrice());

        Product product = mapper.map(productPostDto, Product.class);
        ProductType productType = productTypeEntityService.getByIdWithControl(product.getProductTypeId());

        BigDecimal priceAfterTax = product.getPrice().add(product.getPrice().divide(BigDecimal.valueOf(100.0)).multiply(BigDecimal.valueOf(productType.getTaxPercentage())));
        productType.setProductCount(productType.getProductCount() + 1);
        updateProductType(productType, priceAfterTax, Optional.empty());

        product.setPriceAfterTax(priceAfterTax);
        product.setProductType(productType.getName());

        product = productEntityService.save(product, false);

        return mapper.map(product, ProductGetDto.class);
    }

    public ProductGetDto update(ProductPatchDto productPatchDto, Long id) {
        validatePrice(productPatchDto.getPrice());

        Product product = productEntityService.getByIdWithControl(id);

        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.map(productPatchDto, product);

        ProductType productType = productTypeEntityService.getByIdWithControl(product.getProductTypeId());

        BigDecimal priceAfterTax = product.getPrice().divide(BigDecimal.valueOf(100.0)).multiply(BigDecimal.valueOf(productType.getTaxPercentage()));
        updateProductType(productType, priceAfterTax, Optional.of(product.getPriceAfterTax().subtract(priceAfterTax)));

        product.setPriceAfterTax(priceAfterTax);
        product.setProductType(productType.getName());
        product = productEntityService.save(product, true);

        return mapper.map(product, ProductGetDto.class);
    }

    private void updateProductType(ProductType productType, BigDecimal priceAfterTax, Optional<BigDecimal> priceDifference) {
        if(productType.getMaxPrice().compareTo(priceAfterTax) < 0)
            productType.setMaxPrice(priceAfterTax);
        if(productType.getMinPrice().compareTo(priceAfterTax) > 0 || productType.getMinPrice().compareTo(BigDecimal.ZERO) == 0)
            productType.setMinPrice(priceAfterTax);

        if (priceDifference.isPresent()) {
            priceAfterTax = priceDifference.get();
        }

        BigDecimal newAverage = productType.getAvgPrice().add(
                priceAfterTax.subtract(productType.getAvgPrice())
                        .divide(BigDecimal.valueOf(productType.getProductCount()))
        );

        productType.setAvgPrice(newAverage);
        productTypeEntityService.save(productType, true);
    }

    private void validateProduct(Product product) {
        if(product == null)
            throw new ItemNotFoundException(ErrorMessage.ITEM_NOT_FOUND, this.getClass().getSimpleName());
    }

    private void isNameUnique(String name) {
        Product product = productEntityService.findByProductNameUpper(name);
        if(product != null)
            throw new BusinessException(ProductErrorMessage.NAME_ALREADY_TAKEN);
    }

    private void validatePrice(BigDecimal price) {
        if (price == null)
            return;
        if(price.compareTo(BigDecimal.valueOf(1.0)) < 0) {
            throw new BusinessException(ProductErrorMessage.PRICE_BELOW_ONE);
        }
    }
}
