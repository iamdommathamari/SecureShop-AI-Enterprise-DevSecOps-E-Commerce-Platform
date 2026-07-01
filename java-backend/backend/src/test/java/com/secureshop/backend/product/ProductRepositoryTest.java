package com.secureshop.backend.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test
    void saveProduct_shouldPersist() {

        Product product = new Product(
                null,
                "Laptop",
                "Gaming Laptop",
                85000.0);

        Product saved = repository.save(product);

        assertNotNull(saved.getId());
        assertEquals("Laptop", saved.getName());
    }
}