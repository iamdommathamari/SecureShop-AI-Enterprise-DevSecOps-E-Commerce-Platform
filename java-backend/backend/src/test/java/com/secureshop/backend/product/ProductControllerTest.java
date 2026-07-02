package com.secureshop.backend.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secureshop.backend.dto.PagedResponse;
import com.secureshop.backend.dto.ProductRequestDTO;
import com.secureshop.backend.dto.ProductResponseDTO;
import com.secureshop.backend.exception.GlobalExceptionHandler;
import com.secureshop.backend.exception.ProductNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import(GlobalExceptionHandler.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService service;

    @Test
    @DisplayName("GET /api/products returns all products")
    void getAllProducts_shouldReturnAllProducts() throws Exception {

        List<ProductResponseDTO> response = List.of(
                new ProductResponseDTO(
                        1L,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0,
                        1L),

                new ProductResponseDTO(
                        2L,
                        "Phone",
                        "Android Phone",
                        25000.0,
                        1L)
        );

        when(service.getAllProducts(any(Pageable.class)))
                .thenReturn(new PagedResponse<>(response, 0, 10, 2, 1, true));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Laptop"))
                .andExpect(jsonPath("$.content[1].name").value("Phone"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    @DisplayName("GET /api/products/{id} returns product")
    void getProductById_shouldReturnProduct() throws Exception {

        ProductResponseDTO response =
                new ProductResponseDTO(
                        1L,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0,
                        1L);

        when(service.getProductById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(85000.0));
    }

    @Test
    @DisplayName("POST /api/products creates product")
    void createProduct_shouldReturnCreatedProduct() throws Exception {

        ProductRequestDTO request =
                new ProductRequestDTO(
                        "Laptop",
                        "Gaming Laptop",
                        85000.0,
                        1L);

        ProductResponseDTO response =
                new ProductResponseDTO(
                        1L,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0,
                        1L);

        when(service.createProduct(any(ProductRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.description").value("Gaming Laptop"))
                .andExpect(jsonPath("$.price").value(85000.0));
    }

    @Test
    @DisplayName("PUT /api/products/{id} updates product")
    void updateProduct_shouldReturnUpdatedProduct() throws Exception {

        ProductRequestDTO request =
                new ProductRequestDTO(
                        "Laptop Pro",
                        "Gaming Laptop RTX",
                        99000.0,
                        1L);

        ProductResponseDTO response =
                new ProductResponseDTO(
                        1L,
                        "Laptop Pro",
                        "Gaming Laptop RTX",
                        99000.0,
                        1L);

        when(service.updateProduct(
                eq(1L),
                any(ProductRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop Pro"))
                .andExpect(jsonPath("$.description").value("Gaming Laptop RTX"))
                .andExpect(jsonPath("$.price").value(99000.0));
    }

    @Test
    @DisplayName("DELETE /api/products/{id}")
    void deleteProduct_shouldReturnNoContent() throws Exception {

        doNothing()
                .when(service)
                .deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /api/products validation failure")
    void createProduct_shouldReturnBadRequest() throws Exception {

        ProductRequestDTO request =
                new ProductRequestDTO(
                        "",
                        "Gaming Laptop",
                        85000.0,
                        1L);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Product name is required"));
    }

    @Test
    @DisplayName("GET /api/products/{id} product not found")
    void getProductById_shouldReturnNotFound() throws Exception {

        when(service.getProductById(100L))
                .thenThrow(new ProductNotFoundException(100L));

        mockMvc.perform(get("/api/products/100"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Product not found with id 100"));
    }
}