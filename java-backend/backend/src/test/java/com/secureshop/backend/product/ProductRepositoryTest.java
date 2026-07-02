package com.secureshop.backend.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest

class ProductRepositoryTest {


    @Autowired
    private ProductRepository repository;

    @Test
    @DisplayName("Save product should persist")
    void saveProduct_shouldPersist() {

        Product product = new Product(
                null,
                "Laptop",
                "Gaming Laptop",
                85000.0
        );

        Product saved = repository.save(product);

        assertNotNull(saved.getId());
        assertEquals("Laptop", saved.getName());
        assertEquals("Gaming Laptop", saved.getDescription());
        assertEquals(85000.0, saved.getPrice());
    }

    @Test
    @DisplayName("Find product by id")
    void findById_shouldReturnProduct() {

        Product saved = repository.save(
                new Product(
                        null,
                        "Keyboard",
                        "Mechanical Keyboard",
                        3500.0
                )
        );

        Product found = repository
                .findById(saved.getId())
                .orElse(null);

        assertNotNull(found);
        assertEquals("Keyboard", found.getName());
    }

    @Test
    @DisplayName("Search products by name")
    void findByNameContainingIgnoreCase_shouldReturnMatchingProducts() {

        repository.save(
                new Product(
                        null,
                        "Gaming Laptop",
                        "RTX Laptop",
                        90000.0
                )
        );

        repository.save(
                new Product(
                        null,
                        "Office Laptop",
                        "Business Laptop",
                        60000.0
                )
        );

        repository.save(
                new Product(
                        null,
                        "Wireless Mouse",
                        "Bluetooth Mouse",
                        1500.0
                )
        );

        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> page =
                repository.findByNameContainingIgnoreCase(
                        "laptop",
                        pageable
                );

        assertEquals(2, page.getTotalElements());

        assertTrue(
                page.getContent()
                        .stream()
                        .allMatch(product ->
                                product.getName()
                                        .toLowerCase()
                                        .contains("laptop"))
        );
    }

    @Test
    @DisplayName("Search products should support pagination")
    void searchProducts_shouldSupportPagination() {

        for (int i = 1; i <= 15; i++) {

            repository.save(
                    new Product(
                            null,
                            "Laptop " + i,
                            "Description " + i,
                            50000.0 + i
                    )
            );
        }

        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> page =
                repository.findByNameContainingIgnoreCase(
                        "Laptop",
                        pageable
                );

        assertEquals(10, page.getContent().size());
        assertEquals(15, page.getTotalElements());
        assertEquals(2, page.getTotalPages());
        assertTrue(page.hasNext());
    }
}