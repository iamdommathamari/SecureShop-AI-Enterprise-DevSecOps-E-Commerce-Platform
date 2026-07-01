package com.secureshop.backend.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secureshop.backend.dto.ProductRequestDTO;
import com.secureshop.backend.dto.ProductResponseDTO;
import com.secureshop.backend.exception.GlobalExceptionHandler;
import com.secureshop.backend.exception.ProductNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

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

    @MockBean
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/products/{id} returns product")
    void getProductById_shouldReturnProduct() throws Exception {

        ProductResponseDTO response =
                new ProductResponseDTO(
                        1L,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0);

        when(service.getProductById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/products/1"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id").value(1))

                .andExpect(jsonPath("$.name")
                        .value("Laptop"))

                .andExpect(jsonPath("$.description")
                        .value("Gaming Laptop"))

                .andExpect(jsonPath("$.price")
                        .value(85000.0));
    }

    @Test
    @DisplayName("GET /api/products returns all products")
    void getAllProducts_shouldReturnAllProducts() throws Exception {

        List<ProductResponseDTO> products = List.of(

                new ProductResponseDTO(
                        1L,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0),

                new ProductResponseDTO(
                        2L,
                        "Phone",
                        "Android Phone",
                        25000.0)
        );

        when(service.getAllProducts())
                .thenReturn(products);

        mockMvc.perform(get("/api/products"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.length()").value(2))

                .andExpect(jsonPath("$[0].name")
                        .value("Laptop"))

                .andExpect(jsonPath("$[1].name")
                        .value("Phone"));
    }

    @Test
    @DisplayName("POST /api/products creates product")
    void createProduct_shouldReturnCreatedProduct() throws Exception {

        ProductRequestDTO request =
                new ProductRequestDTO(
                        "Laptop",
                        "Gaming Laptop",
                        85000.0);

        ProductResponseDTO response =
                new ProductResponseDTO(
                        1L,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0);

        when(service.createProduct(request))
                .thenReturn(response);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.id")
                        .value(1))

                .andExpect(jsonPath("$.name")
                        .value("Laptop"))

                .andExpect(jsonPath("$.description")
                        .value("Gaming Laptop"))

                .andExpect(jsonPath("$.price")
                        .value(85000.0));
    }

    @Test
    @DisplayName("PUT /api/products/{id} updates product")
    void updateProduct_shouldReturnUpdatedProduct() throws Exception {

        ProductRequestDTO request =
                new ProductRequestDTO(
                        "Laptop Pro",
                        "Gaming Laptop RTX",
                        99000.0);

        ProductResponseDTO response =
                new ProductResponseDTO(
                        1L,
                        "Laptop Pro",
                        "Gaming Laptop RTX",
                        99000.0);

        when(service.updateProduct(1L, request))
                .thenReturn(response);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id")
                        .value(1))

                .andExpect(jsonPath("$.name")
                        .value("Laptop Pro"))

                .andExpect(jsonPath("$.description")
                        .value("Gaming Laptop RTX"))

                .andExpect(jsonPath("$.price")
                        .value(99000.0));
    }

    @Test
    @DisplayName("DELETE /api/products/{id} deletes product")
    void deleteProduct_shouldReturnNoContent() throws Exception {

        doNothing()
                .when(service)
                .deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1"))

                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /api/products returns 400 for invalid request")
    void createProduct_shouldReturnBadRequest() throws Exception {

        ProductRequestDTO request =
                new ProductRequestDTO(
                        "",
                        "",
                        -100.0);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isBadRequest())

                .andExpect(jsonPath("$.status")
                        .value(400))

                .andExpect(jsonPath("$.message")
                        .value("Product name is required"));
    }

    @Test
    @DisplayName("GET /api/products/{id} returns 404 when product is missing")
    void getProductById_shouldReturnNotFound() throws Exception {

        when(service.getProductById(100L))
                .thenThrow(new ProductNotFoundException(100L));

        mockMvc.perform(get("/api/products/100"))

                .andExpect(status().isNotFound())

                .andExpect(jsonPath("$.status")
                        .value(404));
    }

}