package com.secureshop.backend.product;

import com.secureshop.backend.exception.ProductNotFoundException;
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

    @InjectMocks
    private ProductServiceImpl service;

    @Test
    void createProduct_shouldSaveProduct() {

        Product product =
                new Product(
                        null,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0);

        Product savedProduct =
                new Product(
                        1L,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0);

        when(repository.save(product))
                .thenReturn(savedProduct);

        Product result =
                service.createProduct(product);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop", result.getName());

        verify(repository, times(1))
                .save(product);
    }

    @Test
    void getAllProducts_shouldReturnAllProducts() {

        List<Product> products = List.of(
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

        when(repository.findAll())
                .thenReturn(products);

        List<Product> result =
                service.getAllProducts();

        assertEquals(2, result.size());

        verify(repository, times(1))
                .findAll();
    }

    @Test
    void getProductById_shouldReturnProduct() {

        Product product =
                new Product(
                        1L,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0);

        when(repository.findById(1L))
                .thenReturn(Optional.of(product));

        Product result =
                service.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(repository)
                .findById(1L);
    }

    @Test
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
    void updateProduct_shouldUpdateProduct() {

        Product existing =
                new Product(
                        1L,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0);

        Product updated =
                new Product(
                        null,
                        "Laptop Pro",
                        "Gaming Laptop RTX",
                        99000.0);

        Product saved =
                new Product(
                        1L,
                        "Laptop Pro",
                        "Gaming Laptop RTX",
                        99000.0);

        when(repository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(repository.save(existing))
                .thenReturn(saved);

        Product result =
                service.updateProduct(1L, updated);

        assertEquals("Laptop Pro", result.getName());
        assertEquals(99000.0, result.getPrice());

        verify(repository)
                .findById(1L);

        verify(repository)
                .save(existing);
    }

    @Test
    void deleteProduct_shouldDeleteProduct() {

        Product product =
                new Product(
                        1L,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0);

        when(repository.findById(1L))
                .thenReturn(Optional.of(product));

        doNothing()
                .when(repository)
                .delete(product);

        service.deleteProduct(1L);

        verify(repository)
                .findById(1L);

        verify(repository)
                .delete(product);
    }

}