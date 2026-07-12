package com.secureshop.backend.category;

import com.secureshop.backend.dto.CategoryRequestDTO;
import com.secureshop.backend.dto.CategoryResponseDTO;
import com.secureshop.backend.dto.PagedResponse;
import com.secureshop.backend.exception.CategoryNotFoundException;
import com.secureshop.backend.mapper.CategoryMapper;
import com.secureshop.backend.product.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository repository;

    @Mock
    private CategoryMapper mapper;

    @InjectMocks
    private CategoryServiceImpl service;

    private Category category;
    private CategoryRequestDTO request;
    private CategoryResponseDTO response;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        Product product = Product.builder()
                .id(1L)
                .name("Laptop")
                .description("Gaming Laptop")
                .price(85000.0)
                .build();

        category = Category.builder()
                .id(1L)
                .name("Electronics")
                .description("Electronic Products")
                .products(new ArrayList<>(List.of(product)))
                .build();

        product.setCategory(category);

        request = new CategoryRequestDTO(
                "Electronics",
                "Electronic Products");

        response = new CategoryResponseDTO(
                1L,
                "Electronics",
                "Electronic Products",
                1);
    }

    @Test
    @DisplayName("getAllCategories")
    void getAllCategories_shouldReturnPagedCategories() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<Category> page =
                new PageImpl<>(List.of(category));

        when(repository.findAll(pageable))
                .thenReturn(page);

        when(mapper.toResponse(category))
                .thenReturn(response);

        PagedResponse<CategoryResponseDTO> result =
                service.getAllCategories(pageable);

        assertEquals(
                1,
                result.getContent().size());

        assertEquals(
                "Electronics",
                result.getContent().getFirst().getName());

        verify(repository)
                .findAll(pageable);
    }

    @Test
    @DisplayName("searchCategories")
    void searchCategories_shouldReturnCategories() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<Category> page =
                new PageImpl<>(List.of(category));

        when(repository.findByNameContainingIgnoreCase(
                "Elect",
                pageable))
                .thenReturn(page);

        when(mapper.toResponse(category))
                .thenReturn(response);

        PagedResponse<CategoryResponseDTO> result =
                service.searchCategories(
                        "Elect",
                        pageable);

        assertEquals(
                1,
                result.getContent().size());

        verify(repository)
                .findByNameContainingIgnoreCase(
                        "Elect",
                        pageable);
    }

    @Test
    @DisplayName("getCategoryById")
    void getCategoryById_shouldReturnCategory() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(category));

        when(mapper.toResponse(category))
                .thenReturn(response);

        CategoryResponseDTO result =
                service.getCategoryById(1L);

        assertEquals(
                "Electronics",
                result.getName());

        assertEquals(
                1,
                result.getProductCount());

        verify(repository)
                .findById(1L);
    }

    @Test
    @DisplayName("getCategoryById not found")
    void getCategoryById_shouldThrowException() {

        when(repository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                CategoryNotFoundException.class,
                () -> service.getCategoryById(100L));

        verify(repository)
                .findById(100L);
    }
        @Test
    @DisplayName("createCategory")
    void createCategory_shouldSaveCategory() {

        when(mapper.toEntity(request))
                .thenReturn(category);

        when(repository.save(category))
                .thenReturn(category);

        when(mapper.toResponse(category))
                .thenReturn(response);

        CategoryResponseDTO result =
                service.createCategory(request);

        assertNotNull(result);
        assertEquals(
                "Electronics",
                result.getName());

        verify(repository)
                .save(category);
    }

    @Test
    @DisplayName("updateCategory")
    void updateCategory_shouldUpdateCategory() {

        CategoryRequestDTO updateRequest =
                new CategoryRequestDTO(
                        "Electronics Updated",
                        "Updated Description");

        Category updatedCategory =
                Category.builder()
                        .id(1L)
                        .name("Electronics Updated")
                        .description("Updated Description")
                        .products(category.getProducts())
                        .build();

        CategoryResponseDTO updatedResponse =
                new CategoryResponseDTO(
                        1L,
                        "Electronics Updated",
                        "Updated Description",
                        1);

        when(repository.findById(1L))
                .thenReturn(Optional.of(category));

        when(repository.save(any(Category.class)))
                .thenReturn(updatedCategory);

        when(mapper.toResponse(updatedCategory))
                .thenReturn(updatedResponse);

        CategoryResponseDTO result =
                service.updateCategory(
                        1L,
                        updateRequest);

        assertEquals(
                "Electronics Updated",
                result.getName());

        assertEquals(
                "Updated Description",
                result.getDescription());

        verify(repository)
                .save(category);
    }

    @Test
    @DisplayName("updateCategory not found")
    void updateCategory_shouldThrowException() {

        when(repository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                CategoryNotFoundException.class,
                () -> service.updateCategory(
                        100L,
                        request));

        verify(repository, never())
                .save(any(Category.class));
    }

    @Test
    @DisplayName("deleteCategory")
    void deleteCategory_shouldDeleteCategory() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(category));

        doNothing()
                .when(repository)
                .delete(category);

        service.deleteCategory(1L);

        verify(repository)
                .delete(category);
    }

    @Test
    @DisplayName("deleteCategory not found")
    void deleteCategory_shouldThrowException() {

        when(repository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                CategoryNotFoundException.class,
                () -> service.deleteCategory(100L));

        verify(repository, never())
                .delete(any(Category.class));
    }
}