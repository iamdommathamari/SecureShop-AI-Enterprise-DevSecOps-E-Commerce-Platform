package com.secureshop.backend.category;

import com.secureshop.backend.dto.CategoryRequestDTO;
import com.secureshop.backend.dto.CategoryResponseDTO;
import com.secureshop.backend.dto.PagedResponse;
import com.secureshop.backend.exception.CategoryNotFoundException;
import com.secureshop.backend.mapper.CategoryMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

        category = new Category(
                1L,
                "Electronics",
                "Electronic Products");

        request = new CategoryRequestDTO(
                "Electronics",
                "Electronic Products");

        response = new CategoryResponseDTO(
                1L,
                "Electronics",
                "Electronic Products");
    }

    @Test
    @DisplayName("Should return all categories")
    void getAllCategories_shouldReturnPagedResponse() {

        Page<Category> page = new PageImpl<>(
                List.of(category));

        when(repository.findAll(any(PageRequest.class)))
                .thenReturn(page);

        when(mapper.toResponse(category))
                .thenReturn(response);

        PagedResponse<CategoryResponseDTO> result =
                service.getAllCategories(
                        PageRequest.of(0,10));

        assertEquals(1,
                result.getContent().size());

        assertEquals(
                "Electronics",
                result.getContent().get(0).getName());

        verify(repository)
                .findAll(any(PageRequest.class));
    }

    @Test
    @DisplayName("Should search categories")
    void searchCategories_shouldReturnPagedResponse() {

        Page<Category> page =
                new PageImpl<>(List.of(category));

        when(repository.findByNameContainingIgnoreCase(
                eq("Elect"),
                any(PageRequest.class)))
                .thenReturn(page);

        when(mapper.toResponse(category))
                .thenReturn(response);

        PagedResponse<CategoryResponseDTO> result =
                service.searchCategories(
                        "Elect",
                        PageRequest.of(0,10));

        assertEquals(1,
                result.getContent().size());

        verify(repository)
                .findByNameContainingIgnoreCase(
                        eq("Elect"),
                        any(PageRequest.class));
    }

    @Test
    @DisplayName("Should return category by id")
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
    }

    @Test
    @DisplayName("Should throw exception when category not found")
    void getCategoryById_shouldThrowException() {

        when(repository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                CategoryNotFoundException.class,
                () -> service.getCategoryById(100L));
    }

    @Test
    @DisplayName("Should create category")
    void createCategory_shouldReturnCreatedCategory() {

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
    @DisplayName("Should update category")
    void updateCategory_shouldReturnUpdatedCategory() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(category));

        when(repository.save(any(Category.class)))
                .thenReturn(category);

        when(mapper.toResponse(category))
                .thenReturn(response);

        CategoryResponseDTO result =
                service.updateCategory(
                        1L,
                        request);

        assertEquals(
                "Electronics",
                result.getName());

        verify(repository)
                .save(any(Category.class));
    }

    @Test
    @DisplayName("Should delete category")
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
}