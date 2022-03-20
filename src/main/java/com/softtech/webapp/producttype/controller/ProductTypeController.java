package com.softtech.webapp.producttype.controller;

import com.softtech.webapp.producttype.dto.*;
import com.softtech.webapp.producttype.service.ProductTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/v1/product-types")
@RequiredArgsConstructor
public class ProductTypeController {
    private final ProductTypeService productTypeService;

    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(required = false) String name) {
        if (name != null) {
            ProductTypeGetDto productTypeGetDto = productTypeService.findByName(name);
            return ResponseEntity.ok().body(productTypeGetDto);
        }

        List<ProductTypeGetDto> productTypeGetDtoList = productTypeService.findAll();
        return ResponseEntity.ok().body(productTypeGetDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        ProductTypeGetDto productTypeGetDto = productTypeService.findById(id);
        return ResponseEntity.ok().body(productTypeGetDto);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid ProductTypePostDto productTypePostDto) {
        ProductTypeGetDto productTypeGetDto = productTypeService.save(productTypePostDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productTypeGetDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid ProductTypePatchDto productTypePatchDto) {
        ProductTypeGetDto productTypeGetDto = productTypeService.update(productTypePatchDto, id);
        return ResponseEntity.ok().body(productTypeGetDto);
    }
}
