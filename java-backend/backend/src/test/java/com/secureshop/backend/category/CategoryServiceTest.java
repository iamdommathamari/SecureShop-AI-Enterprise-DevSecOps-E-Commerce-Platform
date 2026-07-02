package com.secureshop.backend.category;

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
class CategoryServiceTest {

    @Mock
    private CategoryRepository repository;

    @Mock
    private CategoryMapper mapper;

    @InjectMocks
    private CategoryServiceImpl service;

    @Test
    void getAllCategories_shouldReturnAllCategories() {
        Category electronics = new Category(1L, "Electronics", "Devices and accessories");
        Category home = new Category(2L, "Home", "Home and kitchen products");

        CategoryResponseDTO electronicsDto = new CategoryResponseDTO(1L, "Electronics", "Devices and accessories");
        CategoryResponseDTO homeDto = new CategoryResponseDTO(2L, "Home", "Home and kitchen products");

        when(repository.findAll()).thenReturn(List.of(electronics, home));
        when(mapper.toResponse(electronics)).thenReturn(electronicsDto);
        when(mapper.toResponse(home)).thenReturn(homeDto);

        List<CategoryResponseDTO> result = service.getAllCategories();

        assertEquals(2, result.size());
        assertEquals("Electronics", result.get(0).getName());
        assertEquals("Home", result.get(1).getName());

        verify(repository).findAll();
    }

    @Test
    void getCategoryById_shouldReturnCategory() {
        Category category = new Category(1L, "Electronics", "Devices and accessories");
        CategoryResponseDTO response = new CategoryResponseDTO(1L, "Electronics", "Devices and accessories");

        when(repository.findById(1L)).thenReturn(Optional.of(category));
        when(mapper.toResponse(category)).thenReturn(response);

        CategoryResponseDTO result = service.getCategoryById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Electronics", result.getName());

        verify(repository).findById(1L);
        verify(mapper).toResponse(category);
    }

    @Test
    void createCategory_shouldSaveCategory() {
        CategoryRequestDTO request = new CategoryRequestDTO("Electronics", "Devices and accessories");
        Category category = new Category(null, "Electronics", "Devices and accessories");
        Category saved = new Category(1L, "Electronics", "Devices and accessories");
        CategoryResponseDTO response = new CategoryResponseDTO(1L, "Electronics", "Devices and accessories");

        when(mapper.toEntity(request)).thenReturn(category);
        when(repository.save(category)).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(response);

        CategoryResponseDTO result = service.createCategory(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Electronics", result.getName());

        verify(mapper).toEntity(request);
        verify(repository).save(category);
        verify(mapper).toResponse(saved);
    }

    @Test
    void updateCategory_shouldUpdateCategory() {
        CategoryRequestDTO request = new CategoryRequestDTO("Electronics", "Updated description");
        Category category = new Category(1L, "Electronics", "Devices and accessories");
        Category updated = new Category(1L, "Electronics", "Updated description");
        CategoryResponseDTO response = new CategoryResponseDTO(1L, "Electronics", "Updated description");

        when(repository.findById(1L)).thenReturn(Optional.of(category));
        when(repository.save(category)).thenReturn(updated);
        when(mapper.toResponse(updated)).thenReturn(response);

        CategoryResponseDTO result = service.updateCategory(1L, request);

        assertNotNull(result);
        assertEquals("Updated description", result.getDescription());

        verify(repository).findById(1L);
        verify(repository).save(category);
        verify(mapper).toResponse(updated);
    }

    @Test
    void deleteCategory_shouldDeleteCategory() {
        Category category = new Category(1L, "Electronics", "Devices and accessories");

        when(repository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(repository).delete(category);

        service.deleteCategory(1L);

        verify(repository).findById(1L);
        verify(repository).delete(category);
    }
}
