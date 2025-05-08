package com.ing.store.controller;

import com.ing.store.dto.ProductDto;
import com.ing.store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public PagedModel<ProductDto> getProductPage(Pageable pageable) {
        return new PagedModel<>(productService.getProductPage(pageable));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ProductDto create(@RequestBody ProductDto productDto) {
        return productService.create(productDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}")
    public ProductDto update(@PathVariable String id, @RequestBody ProductDto productDto) {
        return productService.update(id, productDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        productService.deleteById(id);
    }
}
