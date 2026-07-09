package com.secureshop.backend.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secureshop.backend.dto.PagedResponse;
import com.secureshop.backend.dto.ProductRequestDTO;
import com.secureshop.backend.dto.ProductResponseDTO;
import com.secureshop.backend.exception.GlobalExceptionHandler;
import com.secureshop.backend.exception.ProductNotFoundException;
import com.secureshop.backend.security.JwtAuthenticationEntryPoint;
import com.secureshop.backend.security.JwtAuthenticationFilter;
import com.secureshop.backend.security.JwtService;
import com.secureshop.backend.security.UserDetailsServiceImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import com.secureshop.backend.config.SecurityConfig;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Mock the service used by this controller
    @MockitoBean
    private ProductService productService;

    // Security beans
    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @MockitoBean
    private JwtService jwtService;

    @MockBean
    private ProductService service;

    @Test
    @DisplayName("GET /api/products")
    void getAllProducts_shouldReturnPagedProducts()
            throws Exception {

        ProductResponseDTO product =
                new ProductResponseDTO(
                        1L,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0,
                        1L,
                        "Electronics");

        PagedResponse<ProductResponseDTO> response =
                new PagedResponse<>(
                        List.of(product),
                        0,
                        10,
                        1,
                        1,
                        true);

        when(service.getAllProducts(any(Pageable.class)))
                .thenReturn(response);

        mockMvc.perform(get("/api/products"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.content.length()")
                        .value(1))

                .andExpect(jsonPath("$.content[0].name")
                        .value("Laptop"))

                .andExpect(jsonPath("$.content[0].categoryName")
                        .value("Electronics"));
    }

    @Test
    @DisplayName("GET /api/products/search")
    void searchProducts_shouldReturnProducts()
            throws Exception {

        ProductResponseDTO product =
                new ProductResponseDTO(
                        1L,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0,
                        1L,
                        "Electronics");

        PagedResponse<ProductResponseDTO> response =
                new PagedResponse<>(
                        List.of(product),
                        0,
                        10,
                        1,
                        1,
                        true);

        when(service.searchProducts(
                eq("Laptop"),
                any(Pageable.class)))
                .thenReturn(response);

        mockMvc.perform(
                get("/api/products/search")
                        .param("keyword", "Laptop"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.content[0].name")
                        .value("Laptop"));
    }

    @Test
    @DisplayName("GET /api/products/category/{id}")
    void getProductsByCategory_shouldReturnProducts()
            throws Exception {

        ProductResponseDTO product =
                new ProductResponseDTO(
                        1L,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0,
                        1L,
                        "Electronics");

        PagedResponse<ProductResponseDTO> response =
                new PagedResponse<>(
                        List.of(product),
                        0,
                        10,
                        1,
                        1,
                        true);

        when(service.getProductsByCategory(
                eq(1L),
                any(Pageable.class)))
                .thenReturn(response);

        mockMvc.perform(
                get("/api/products/category/1"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.content[0].categoryId")
                        .value(1))

                .andExpect(jsonPath("$.content[0].categoryName")
                        .value("Electronics"));
    }

    @Test
    @DisplayName("GET /api/products/{id}")
    void getProductById_shouldReturnProduct()
            throws Exception {

        ProductResponseDTO response =
                new ProductResponseDTO(
                        1L,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0,
                        1L,
                        "Electronics");

        when(service.getProductById(1L))
                .thenReturn(response);

        mockMvc.perform(
                get("/api/products/1"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id")
                        .value(1))

                .andExpect(jsonPath("$.categoryName")
                        .value("Electronics"));
    }

    @Test
    @DisplayName("POST /api/products")
    void createProduct_shouldReturnCreatedProduct()
            throws Exception {

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
                        1L,
                        "Electronics");

        when(service.createProduct(any(ProductRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(
                post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.id")
                        .value(1))

                .andExpect(jsonPath("$.categoryId")
                        .value(1));
    }
        @Test
    @DisplayName("PUT /api/products/{id}")
    void updateProduct_shouldReturnUpdatedProduct()
            throws Exception {

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
                        1L,
                        "Electronics");

        when(service.updateProduct(
                eq(1L),
                any(ProductRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(
                put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.name")
                        .value("Laptop Pro"))

                .andExpect(jsonPath("$.price")
                        .value(99000.0))

                .andExpect(jsonPath("$.categoryName")
                        .value("Electronics"));
    }

    @Test
    @DisplayName("DELETE /api/products/{id}")
    void deleteProduct_shouldReturnNoContent()
            throws Exception {

        doNothing()
                .when(service)
                .deleteProduct(1L);

        mockMvc.perform(
                delete("/api/products/1"))

                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST validation failure")
    void createProduct_shouldReturnBadRequest()
            throws Exception {

        ProductRequestDTO request =
                new ProductRequestDTO(
                        "",
                        "",
                        -100.0,
                        null);

        mockMvc.perform(
                post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isBadRequest())

                .andExpect(jsonPath("$.status")
                        .value(400));
    }

    @Test
    @DisplayName("GET product not found")
    void getProductById_shouldReturnNotFound()
            throws Exception {

        when(service.getProductById(100L))
                .thenThrow(new ProductNotFoundException(100L));

        mockMvc.perform(
                get("/api/products/100"))

                .andExpect(status().isNotFound())

                .andExpect(jsonPath("$.status")
                        .value(404))

                .andExpect(jsonPath("$.message")
                        .value("Product not found with id 100"));
    }
}