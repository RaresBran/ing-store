package com.ing.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.store.dto.ProductDto;
import com.ing.store.model.entity.Product;
import com.ing.store.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ProductRepository productRepository;

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void shouldCreateProduct() throws Exception {
        ProductDto productDto = new ProductDto(null, "Test Product", 19.99, 100);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Product")))
                .andExpect(jsonPath("$.price", is(19.99)))
                .andExpect(jsonPath("$.stock", is(100)));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void shouldUpdateProduct() throws Exception {
        Product product = Product.builder()
                .name("Old Name")
                .price(10.0)
                .stock(5)
                .build();
        Product saved = productRepository.save(product);

        ProductDto updatedDto = new ProductDto(saved.getId(), "New Name", 12.5, 10);

        mockMvc.perform(patch("/products/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("New Name")))
                .andExpect(jsonPath("$.stock", is(10)));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void shouldDeleteProduct() throws Exception {
        Product product = Product.builder()
                .name("To Be Deleted")
                .price(5.0)
                .stock(1)
                .build();
        Product saved = productRepository.save(product);

        mockMvc.perform(delete("/products/" + saved.getId()))
                .andExpect(status().isOk());

        assertFalse(productRepository.findById(saved.getId()).isPresent());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    void shouldGetProducts() throws Exception {
        productRepository.save(Product.builder().name("Public Product").price(3.5).stock(50).build());

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Public Product")));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void shouldReturn404WhenUpdatingNonExistentProduct() throws Exception {
        String nonExistentId = "nonexistent123";
        ProductDto productDto = new ProductDto(nonExistentId, "Test Product", 19.99, 100);

        mockMvc.perform(patch("/products/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.businessErrorCode", is(404)))
                .andExpect(jsonPath("$.businessErrorDescription", is("Requested entity does not exist!")));
    }
}