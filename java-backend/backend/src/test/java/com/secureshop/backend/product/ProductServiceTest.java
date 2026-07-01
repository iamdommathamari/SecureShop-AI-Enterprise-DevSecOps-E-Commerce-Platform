package com.secureshop.backend.product;

import com.secureshop.backend.dto.ProductRequestDTO;
import com.secureshop.backend.dto.ProductResponseDTO;
import com.secureshop.backend.exception.ProductNotFoundException;
import com.secureshop.backend.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductServiceImpl service;

    @Test
    void createProduct_shouldSaveProduct() {

        ProductRequestDTO request =
                new ProductRequestDTO(
                        "Laptop",
                        "Gaming Laptop",
                        85000.0);

        Product entity =
                new Product(
                        null,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0);

        Product savedEntity =
                new Product(
                        1L,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0);

        ProductResponseDTO response =
                new ProductResponseDTO(
                        1L,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0);

        when(mapper.toEntity(request))
                .thenReturn(entity);

        when(repository.save(entity))
                .thenReturn(savedEntity);

        when(mapper.toResponse(savedEntity))
                .thenReturn(response);

        ProductResponseDTO result =
                service.createProduct(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop", result.getName());

        verify(mapper).toEntity(request);
        verify(repository).save(entity);
        verify(mapper).toResponse(savedEntity);
    }

    @Test
    void getAllProducts_shouldReturnAllProducts() {

        List<Product> entities = List.of(

                new Product(
                        1L,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0),

                new Product(
                        2L,
                        "Phone",
                        "Android Phone",
                        25000.0)
        );

        ProductResponseDTO laptop =
                new ProductResponseDTO(
                        1L,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0);

        ProductResponseDTO phone =
                new ProductResponseDTO(
                        2L,
                        "Phone",
                        "Android Phone",
                        25000.0);

        when(repository.findAll())
                .thenReturn(entities);

        when(mapper.toResponse(entities.get(0)))
                .thenReturn(laptop);

        when(mapper.toResponse(entities.get(1)))
                .thenReturn(phone);

        List<ProductResponseDTO> result =
                service.getAllProducts();

        assertEquals(2, result.size());
        assertEquals("Laptop", result.get(0).getName());
        assertEquals("Phone", result.get(1).getName());

        verify(repository).findAll();
    }

    @Test
    void getProductById_shouldReturnProduct() {

        Product entity =
                new Product(
                        1L,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0);

        ProductResponseDTO response =
                new ProductResponseDTO(
                        1L,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0);

        when(repository.findById(1L))
                .thenReturn(Optional.of(entity));

        when(mapper.toResponse(entity))
                .thenReturn(response);

        ProductResponseDTO result =
                service.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(repository).findById(1L);
        verify(mapper).toResponse(entity);
    }

    @Test
    void getProductById_shouldThrowException() {

        when(repository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> service.getProductById(100L));

        verify(repository).findById(100L);

        verify(mapper, never()).toResponse(any());
    }

    @Test
    void updateProduct_shouldUpdateProduct() {

        ProductRequestDTO request =
                new ProductRequestDTO(
                        "Laptop Pro",
                        "Gaming Laptop RTX",
                        99000.0);

        Product entity =
                new Product(
                        1L,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0);

        Product saved =
                new Product(
                        1L,
                        "Laptop Pro",
                        "Gaming Laptop RTX",
                        99000.0);

        ProductResponseDTO response =
                new ProductResponseDTO(
                        1L,
                        "Laptop Pro",
                        "Gaming Laptop RTX",
                        99000.0);

        when(repository.findById(1L))
                .thenReturn(Optional.of(entity));

        when(repository.save(entity))
                .thenReturn(saved);

        when(mapper.toResponse(saved))
                .thenReturn(response);

        ProductResponseDTO result =
                service.updateProduct(1L, request);

        assertEquals("Laptop Pro", result.getName());
        assertEquals(99000.0, result.getPrice());

        verify(repository).findById(1L);
        verify(repository).save(entity);
        verify(mapper).toResponse(saved);
    }

    @Test
    void deleteProduct_shouldDeleteProduct() {

        Product entity =
                new Product(
                        1L,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0);

        when(repository.findById(1L))
                .thenReturn(Optional.of(entity));

        doNothing()
                .when(repository)
                .delete(entity);

        service.deleteProduct(1L);

        verify(repository).findById(1L);
        verify(repository).delete(entity);

        verifyNoInteractions(mapper);
    }
}