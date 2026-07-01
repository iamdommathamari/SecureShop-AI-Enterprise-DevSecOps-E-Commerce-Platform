package com.secureshop.backend.product;

import com.secureshop.backend.exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

        Product product = new Product(
            null,
            "Laptop",
            "Gaming Laptop",
            85000.0
        );

        Product savedProduct = new Product(
            1L,
            "Laptop",
            "Gaming Laptop",
            85000.0
        );

        when(repository.save(product))
            .thenReturn(savedProduct);

        Product result = service.createProduct(product);

        assertNotNull(result);

        assertEquals(1L, result.getId());

        assertEquals("Laptop", result.getName());

        verify(repository, times(1))
            .save(product);
    }

}