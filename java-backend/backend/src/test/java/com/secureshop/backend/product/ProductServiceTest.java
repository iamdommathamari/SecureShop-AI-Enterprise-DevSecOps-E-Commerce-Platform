package com.secureshop.backend.product;

import com.secureshop.backend.dto.PagedResponse;
import com.secureshop.backend.dto.ProductRequestDTO;
import com.secureshop.backend.dto.ProductResponseDTO;
import com.secureshop.backend.exception.ProductNotFoundException;
import com.secureshop.backend.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Get all products returns paged response")
    void getAllProducts_shouldReturnPagedResponse() {

        Product product = new Product(
                1L,
                "Laptop",
                "Gaming Laptop",
                85000.0
        );

        ProductResponseDTO response =
                new ProductResponseDTO(
                        1L,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0
                );

        Pageable pageable = PageRequest.of(0,10);

        Page<Product> page =
                new PageImpl<>(
                        List.of(product),
                        pageable,
                        1
                );

        when(repository.findAll(pageable))
                .thenReturn(page);

        when(mapper.toResponse(product))
                .thenReturn(response);

        PagedResponse<ProductResponseDTO> result =
                service.getAllProducts(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Laptop",
                result.getContent().get(0).getName());

        verify(repository).findAll(pageable);
    }

    @Test
    @DisplayName("Search products by keyword")
    void searchProducts_shouldReturnPagedResponse() {

        Product product =
                new Product(
                        1L,
                        "Gaming Laptop",
                        "RTX",
                        90000.0
                );

        ProductResponseDTO response =
                new ProductResponseDTO(
                        1L,
                        "Gaming Laptop",
                        "RTX",
                        90000.0
                );

        Pageable pageable = PageRequest.of(0,10);

        Page<Product> page =
                new PageImpl<>(
                        List.of(product),
                        pageable,
                        1
                );

        when(repository.findByNameContainingIgnoreCase(
                "Laptop",
                pageable))
                .thenReturn(page);

        when(mapper.toResponse(product))
                .thenReturn(response);

        PagedResponse<ProductResponseDTO> result =
                service.searchProducts(
                        "Laptop",
                        pageable);

        assertEquals(1,
                result.getContent().size());

        assertEquals(
                "Gaming Laptop",
                result.getContent().get(0).getName());

        verify(repository)
                .findByNameContainingIgnoreCase(
                        "Laptop",
                        pageable);
    }

    @Test
    @DisplayName("Get product by id")
    void getProductById_shouldReturnProduct() {

        Product product =
                new Product(
                        1L,
                        "Laptop",
                        "Gaming",
                        85000.0
                );

        ProductResponseDTO response =
                new ProductResponseDTO(
                        1L,
                        "Laptop",
                        "Gaming",
                        85000.0
                );

        when(repository.findById(1L))
                .thenReturn(Optional.of(product));

        when(mapper.toResponse(product))
                .thenReturn(response);

        ProductResponseDTO result =
                service.getProductById(1L);

        assertEquals(
                "Laptop",
                result.getName());

        verify(repository)
                .findById(1L);
    }

    @Test
    @DisplayName("Get product by invalid id throws exception")
    void getProductById_shouldThrowException() {

        when(repository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> service.getProductById(100L));

        verify(repository)
                .findById(100L);
    }

    @Test
    @DisplayName("Create product")
    void createProduct_shouldSaveProduct() {

        ProductRequestDTO request =
                new ProductRequestDTO(
                        "Laptop",
                        "Gaming",
                        85000.0
                );

        Product entity =
                new Product(
                        null,
                        "Laptop",
                        "Gaming",
                        85000.0
                );

        Product saved =
                new Product(
                        1L,
                        "Laptop",
                        "Gaming",
                        85000.0
                );

        ProductResponseDTO response =
                new ProductResponseDTO(
                        1L,
                        "Laptop",
                        "Gaming",
                        85000.0
                );

        when(mapper.toEntity(request))
                .thenReturn(entity);

        when(repository.save(entity))
                .thenReturn(saved);

        when(mapper.toResponse(saved))
                .thenReturn(response);

        ProductResponseDTO result =
                service.createProduct(request);

        assertEquals(
                1L,
                result.getId());

        verify(repository)
                .save(entity);
    }

    @Test
    @DisplayName("Update product")
    void updateProduct_shouldUpdateExistingProduct() {

        Product existing =
                new Product(
                        1L,
                        "Laptop",
                        "Gaming",
                        85000.0
                );

        ProductRequestDTO request =
                new ProductRequestDTO(
                        "Laptop Pro",
                        "RTX",
                        95000.0
                );

        ProductResponseDTO response =
                new ProductResponseDTO(
                        1L,
                        "Laptop Pro",
                        "RTX",
                        95000.0
                );

        when(repository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(repository.save(existing))
                .thenReturn(existing);

        when(mapper.toResponse(existing))
                .thenReturn(response);

        ProductResponseDTO result =
                service.updateProduct(
                        1L,
                        request);

        assertEquals(
                "Laptop Pro",
                result.getName());

        verify(repository)
                .save(existing);
    }

    @Test
    @DisplayName("Delete product")
    void deleteProduct_shouldDeleteExistingProduct() {

        Product product =
                new Product(
                        1L,
                        "Laptop",
                        "Gaming",
                        85000.0
                );

        when(repository.findById(1L))
                .thenReturn(Optional.of(product));

        service.deleteProduct(1L);

        verify(repository)
                .delete(product);
    }

    @Test
    @DisplayName("Delete invalid product throws exception")
    void deleteProduct_shouldThrowException() {

        when(repository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> service.deleteProduct(10L));

        verify(repository)
                .findById(10L);
    }
}