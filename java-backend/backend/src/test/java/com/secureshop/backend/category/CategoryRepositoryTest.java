package com.secureshop.backend.category;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repository;

    private static final String ELECTRONICS = "Electronics";
    private static final String LAPTOPS = "Laptops and Accessories";

    @Test
    @DisplayName("Should save category")
    void save_shouldPersistCategory() {

        Category category = new Category(
                null,
                ELECTRONICS,
                LAPTOPS);

        Category saved = repository.save(category);

        assertNotNull(saved.getId());
        assertEquals(ELECTRONICS, saved.getName());
        assertEquals(LAPTOPS, saved.getDescription());
    }

    @Test
    @DisplayName("Should find category by id")
    void findById_shouldReturnCategory() {

        Category saved = repository.save(
                new Category(
                        null,
                        ELECTRONICS,
                        LAPTOPS));

        Optional<Category> result =
                repository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals(ELECTRONICS, result.get().getName());
    }

    @Test
    @DisplayName("Should return all categories")
    void findAll_shouldReturnCategories() {

        repository.save(
                new Category(
                        null,
                        "Electronics",
                        "Electronic Items"));

        repository.save(
                new Category(
                        null,
                        "Books",
                        "Books Collection"));

        Pageable pageable =
                PageRequest.of(0, 10);

        Page<Category> page =
                repository.findAll(pageable);

        assertEquals(2, page.getTotalElements());
        assertEquals(2, page.getContent().size());
    }

    @Test
    @DisplayName("Should search category by name")
    void findByNameContainingIgnoreCase_shouldReturnMatchingCategories() {

        repository.save(
                new Category(
                        null,
                        "Electronics",
                        "Electronic Items"));

        repository.save(
                new Category(
                        null,
                        "Books",
                        "Books Collection"));

        Pageable pageable =
                PageRequest.of(0, 10);

        Page<Category> page =
                repository.findByNameContainingIgnoreCase(
                        "elect",
                        pageable);

        assertEquals(1, page.getTotalElements());
        assertEquals(
                "Electronics",
                page.getContent().get(0).getName());
    }

    @Test
    @DisplayName("Should delete category")
    void delete_shouldRemoveCategory() {

        Category saved =
                repository.save(
                        new Category(
                                null,
                                ELECTRONICS,
                                LAPTOPS));

        repository.delete(saved);

        Optional<Category> result =
                repository.findById(saved.getId());

        assertFalse(result.isPresent());
    }
}