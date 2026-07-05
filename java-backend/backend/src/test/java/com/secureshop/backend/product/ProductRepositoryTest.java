package com.secureshop.backend.product;

import com.secureshop.backend.category.Category;
import com.secureshop.backend.category.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("save product")
    void saveProduct_shouldPersist() {

        Category category = categoryRepository.save(
                Category.builder()
                        .name("Electronics")
                        .description("Electronic Products")
                        .build());

        Product product = Product.builder()
                .name("Laptop")
                .description("Gaming Laptop")
                .price(85000.0)
                .category(category)
                .build();

        Product saved = repository.save(product);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Laptop");
        assertThat(saved.getCategory().getId())
                .isEqualTo(category.getId());
    }

    @Test
    @DisplayName("findByNameContainingIgnoreCase")
    void findByNameContainingIgnoreCase_shouldReturnMatchingProducts() {

        Category category = categoryRepository.save(
                Category.builder()
                        .name("Electronics")
                        .description("Electronic Products")
                        .build());

        repository.save(
                Product.builder()
                        .name("Gaming Laptop")
                        .description("Laptop")
                        .price(85000.0)
                        .category(category)
                        .build());

        repository.save(
                Product.builder()
                        .name("Phone")
                        .description("Android")
                        .price(25000.0)
                        .category(category)
                        .build());

        Pageable pageable = PageRequest.of(0,10);

        Page<Product> page =
                repository.findByNameContainingIgnoreCase(
                        "laptop",
                        pageable);

        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().getFirst().getName())
                .isEqualTo("Gaming Laptop");
    }

    @Test
    @DisplayName("findByCategoryId")
    void findByCategoryId_shouldReturnProducts() {

        Category laptops = categoryRepository.save(
                Category.builder()
                        .name("Laptops")
                        .description("Laptop Category")
                        .build());

        Category mobiles = categoryRepository.save(
                Category.builder()
                        .name("Mobiles")
                        .description("Mobile Category")
                        .build());

        repository.save(
                Product.builder()
                        .name("MacBook")
                        .description("Apple")
                        .price(200000.0)
                        .category(laptops)
                        .build());

        repository.save(
                Product.builder()
                        .name("Dell XPS")
                        .description("Dell")
                        .price(180000.0)
                        .category(laptops)
                        .build());

        repository.save(
                Product.builder()
                        .name("iPhone")
                        .description("Apple")
                        .price(120000.0)
                        .category(mobiles)
                        .build());

        Pageable pageable = PageRequest.of(0,10);

        Page<Product> page =
                repository.findByCategoryId(
                        laptops.getId(),
                        pageable);

        assertThat(page.getContent()).hasSize(2);
    }

    @Test
    @DisplayName("findByCategoryIdAndNameContainingIgnoreCase")
    void findByCategoryIdAndNameContainingIgnoreCase_shouldReturnProducts() {

        Category laptops = categoryRepository.save(
                Category.builder()
                        .name("Laptops")
                        .description("Laptop Category")
                        .build());

        repository.save(
                Product.builder()
                        .name("MacBook Pro")
                        .description("Apple")
                        .price(200000.0)
                        .category(laptops)
                        .build());

        repository.save(
                Product.builder()
                        .name("Dell XPS")
                        .description("Dell")
                        .price(170000.0)
                        .category(laptops)
                        .build());

        Pageable pageable = PageRequest.of(0,10);

        Page<Product> page =
                repository.findByCategoryIdAndNameContainingIgnoreCase(
                        laptops.getId(),
                        "mac",
                        pageable);

        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().getFirst().getName())
                .isEqualTo("MacBook Pro");
    }

    @Test
    @DisplayName("pagination")
    void pagination_shouldReturnPagedResults() {

        Category category = categoryRepository.save(
                Category.builder()
                        .name("Electronics")
                        .description("Electronic Products")
                        .build());

        for (int i = 1; i <= 20; i++) {

            repository.save(
                    Product.builder()
                            .name("Product " + i)
                            .description("Description")
                            .price(100.0 + i)
                            .category(category)
                            .build());
        }

        Pageable pageable = PageRequest.of(0,5);

        Page<Product> page =
                repository.findAll(pageable);

        assertThat(page.getContent()).hasSize(5);
        assertThat(page.getTotalElements()).isEqualTo(20);
        assertThat(page.getTotalPages()).isEqualTo(4);
    }
}