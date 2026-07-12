package com.secureshop.backend.category;

import com.secureshop.backend.product.Product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CategoryRepositoryTest {

    @Test
    @DisplayName("Category Builder")
    void categoryBuilder_shouldCreateCategory() {

        Category category = Category.builder()
                .id(1L)
                .name("Electronics")
                .description("Electronic Products")
                .products(new ArrayList<>())
                .build();

        assertEquals(1L, category.getId());
        assertEquals("Electronics", category.getName());
        assertEquals("Electronic Products", category.getDescription());
        assertNotNull(category.getProducts());
        assertTrue(category.getProducts().isEmpty());
    }

    @Test
    @DisplayName("Add Product to Category")
    void category_shouldContainProducts() {

        Category category = Category.builder()
                .id(1L)
                .name("Electronics")
                .description("Electronic Products")
                .build();

        Product product = Product.builder()
                .id(1L)
                .name("Laptop")
                .description("Gaming Laptop")
                .price(85000.0)
                .category(category)
                .build();

        category.getProducts().add(product);

        assertEquals(1, category.getProducts().size());
        assertEquals("Laptop",
                category.getProducts().getFirst().getName());
    }

    @Test
    @DisplayName("Product references Category")
    void product_shouldReferenceCategory() {

        Category category = Category.builder()
                .id(10L)
                .name("Electronics")
                .build();

        Product product = Product.builder()
                .id(5L)
                .name("Laptop")
                .description("Gaming Laptop")
                .price(85000.0)
                .category(category)
                .build();

        assertNotNull(product.getCategory());
        assertEquals(
                "Electronics",
                product.getCategory().getName());
    }

    @Test
    @DisplayName("Category products list should never be null")
    void categoryProducts_shouldNotBeNull() {

        Category category = Category.builder()
                .id(1L)
                .name("Electronics")
                .description("Electronic Products")
                .build();

        assertNotNull(category.getProducts());
    }

    @Test
    @DisplayName("Category equals/hashCode")
    void categoryEquals_shouldWork() {

        Category c1 = Category.builder()
                .id(1L)
                .name("Electronics")
                .description("Electronic Products")
                .build();

        Category c2 = Category.builder()
                .id(1L)
                .name("Electronics")
                .description("Electronic Products")
                .build();

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }
}