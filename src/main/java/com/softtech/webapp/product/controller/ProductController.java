package com.softtech.webapp.product.controller;

import com.softtech.webapp.product.dto.*;
import com.softtech.webapp.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @Operation(tags = "Product Controller")
    @GetMapping
    public ResponseEntity<?> findAll(Optional<BigDecimal> minPrice, Optional<BigDecimal> maxPrice, Optional<String> productTypeName) {
        List<ProductGetDto> productGetDtoList = productService.findAll(minPrice, maxPrice, productTypeName);
        return ResponseEntity.ok().body(productGetDtoList);
    }

    @Operation(tags = "Product Controller")
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        ProductGetDto productGetDto = productService.findById(id);
        return ResponseEntity.ok().body(productGetDto);
    }

    @Operation(tags = "Product Controller")
    @GetMapping("/product-names/{name}")
    public ResponseEntity<?> findByName(@PathVariable String name) {
        ProductGetDto productGetDto = productService.findByName(name);
        return ResponseEntity.ok().body(productGetDto);
    }

    @Operation(tags = "Product Controller")
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid ProductPostDto productPostDto) {
        ProductGetDto productGetDto = productService.save(productPostDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productGetDto);
    }

    @Operation(tags = "Product Controller")
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductPatchDto productPatchDto) {
        ProductGetDto productGetDto = productService.update(productPatchDto, id);
        return ResponseEntity.ok().body(productGetDto);
    }
}
