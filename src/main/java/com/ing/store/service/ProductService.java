package com.ing.store.service;

import com.ing.store.dto.ProductDto;
import com.ing.store.model.entity.Product;
import com.ing.store.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public Page<ProductDto> getProductPage(final Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(product -> modelMapper.map(product, ProductDto.class));
    }

    public ProductDto create(ProductDto productDto) {
        try {
            Product product = productRepository.save(modelMapper.map(productDto, Product.class));
            return modelMapper.map(product, ProductDto.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid product data");
        }
    }

    public ProductDto update(String id, ProductDto productDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (productDto.getName() != null) product.setName(productDto.getName());
        if (productDto.getPrice() != null) product.setPrice(productDto.getPrice());
        if (productDto.getStock() != null) product.setStock(productDto.getStock());

        return modelMapper.map(productRepository.save(product), ProductDto.class);
    }

    public void deleteById(String id) {
        productRepository.deleteById(id);
    }
}
