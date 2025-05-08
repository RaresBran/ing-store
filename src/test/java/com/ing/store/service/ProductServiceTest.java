package com.ing.store.service;

import com.ing.store.dto.ProductDto;
import com.ing.store.model.entity.Product;
import com.ing.store.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void getProductPage_shouldReturnMappedPage() {
        Product product = Product.builder().id("123").name("Test").price(10.0).stock(5).build();
        ProductDto productDto = new ProductDto("123", "Test", 10.0, 5);

        when(productRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(product)));
        when(modelMapper.map(product, ProductDto.class)).thenReturn(productDto);

        var result = productService.getProductPage(Pageable.ofSize(10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Test", result.getContent().get(0).getName());
        verify(productRepository).findAll(any(Pageable.class));
    }

    @Test
    void create_shouldSaveAndReturnMappedProduct() {
        ProductDto dto = new ProductDto(null, "Product A", 20.0, 100);
        Product entity = Product.builder().id("abc").name("Product A").price(20.0).stock(100).build();

        when(modelMapper.map(dto, Product.class)).thenReturn(entity);
        when(productRepository.save(entity)).thenReturn(entity);
        when(modelMapper.map(entity, ProductDto.class)).thenReturn(
                new ProductDto("abc", "Product A", 20.0, 100)
        );

        ProductDto result = productService.create(dto);

        assertNotNull(result);
        assertEquals("Product A", result.getName());
    }

    @Test
    void update_shouldModifyAndReturnProduct() {
        Product existing = Product.builder().id("abc").name("Old").price(10.0).stock(50).build();
        ProductDto updateDto = new ProductDto("abc", "Updated", 99.9, 5);
        Product updated = Product.builder().id("abc").name("Updated").price(99.9).stock(5).build();

        when(productRepository.findById("abc")).thenReturn(Optional.of(existing));
        when(productRepository.save(existing)).thenReturn(updated);
        when(modelMapper.map(updated, ProductDto.class)).thenReturn(updateDto);

        ProductDto result = productService.update("abc", updateDto);

        assertEquals("Updated", result.getName());
        assertEquals(99.9, result.getPrice());
    }

    @Test
    void update_shouldThrowIfProductNotFound() {
        when(productRepository.findById("xyz")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.update("xyz", new ProductDto()));
    }

    @Test
    void deleteById_shouldCallRepository() {
        productService.deleteById("abc");
        verify(productRepository).deleteById("abc");
    }

    @Test
    void create_shouldThrowIllegalArgumentExceptionIfProductNameConflict() {
        ProductDto dto = new ProductDto(null, "Duplicate Product", 15.0, 50);

        when(modelMapper.map(dto, Product.class))
                .thenReturn(Product.builder().name("Duplicate Product").price(15.0).stock(50).build());

        // Simulate database exception, e.g. from a unique constraint violation
        when(productRepository.save(any(Product.class)))
                .thenThrow(new RuntimeException("Unique constraint violation"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> productService.create(dto));

        assertEquals("Invalid product data", exception.getMessage());
        verify(productRepository).save(any(Product.class));
    }

}

