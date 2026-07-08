package com.secureshop.backend.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secureshop.backend.dto.CategoryRequestDTO;
import com.secureshop.backend.dto.CategoryResponseDTO;
import com.secureshop.backend.dto.PagedResponse;
import com.secureshop.backend.exception.CategoryNotFoundException;
import com.secureshop.backend.exception.GlobalExceptionHandler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import com.secureshop.backend.config.SecurityConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@Import({
        SecurityConfig.class,
        GlobalExceptionHandler.class
})

class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService service;

    @Test
    @DisplayName("GET /api/categories")
    void getAllCategories_shouldReturnPagedCategories()
            throws Exception {

        CategoryResponseDTO category =
                new CategoryResponseDTO(
                        1L,
                        "Electronics",
                        "Electronic Products",
                        5);

        PagedResponse<CategoryResponseDTO> response =
                new PagedResponse<>(
                        List.of(category),
                        0,
                        10,
                        1,
                        1,
                        true);

        when(service.getAllCategories(any(Pageable.class)))
                .thenReturn(response);

        mockMvc.perform(get("/api/categories"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.content.length()")
                        .value(1))

                .andExpect(jsonPath("$.content[0].name")
                        .value("Electronics"))

                .andExpect(jsonPath("$.content[0].productCount")
                        .value(5));
    }

    @Test
    @DisplayName("GET /api/categories/search")
    void searchCategories_shouldReturnCategories()
            throws Exception {

        CategoryResponseDTO category =
                new CategoryResponseDTO(
                        1L,
                        "Electronics",
                        "Electronic Products",
                        5);

        PagedResponse<CategoryResponseDTO> response =
                new PagedResponse<>(
                        List.of(category),
                        0,
                        10,
                        1,
                        1,
                        true);

        when(service.searchCategories(
                eq("Elect"),
                any(Pageable.class)))
                .thenReturn(response);

        mockMvc.perform(
                get("/api/categories/search")
                        .param("keyword", "Elect"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.content[0].name")
                        .value("Electronics"));
    }

    @Test
    @DisplayName("GET /api/categories/{id}")
    void getCategoryById_shouldReturnCategory()
            throws Exception {

        CategoryResponseDTO response =
                new CategoryResponseDTO(
                        1L,
                        "Electronics",
                        "Electronic Products",
                        5);

        when(service.getCategoryById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/categories/1"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id")
                        .value(1))

                .andExpect(jsonPath("$.name")
                        .value("Electronics"))

                .andExpect(jsonPath("$.productCount")
                        .value(5));
    }

    @Test
    @DisplayName("POST /api/categories")
    void createCategory_shouldReturnCreatedCategory()
            throws Exception {

        CategoryRequestDTO request =
                new CategoryRequestDTO(
                        "Electronics",
                        "Electronic Products");

        CategoryResponseDTO response =
                new CategoryResponseDTO(
                        1L,
                        "Electronics",
                        "Electronic Products",
                        0);

        when(service.createCategory(any(CategoryRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(
                post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.id")
                        .value(1))

                .andExpect(jsonPath("$.name")
                        .value("Electronics"));
    }
        @Test
    @DisplayName("PUT /api/categories/{id}")
    void updateCategory_shouldReturnUpdatedCategory()
            throws Exception {

        CategoryRequestDTO request =
                new CategoryRequestDTO(
                        "Electronics Updated",
                        "Updated Description");

        CategoryResponseDTO response =
                new CategoryResponseDTO(
                        1L,
                        "Electronics Updated",
                        "Updated Description",
                        5);

        when(service.updateCategory(
                eq(1L),
                any(CategoryRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(
                put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.name")
                        .value("Electronics Updated"))

                .andExpect(jsonPath("$.description")
                        .value("Updated Description"))

                .andExpect(jsonPath("$.productCount")
                        .value(5));
    }

    @Test
    @DisplayName("DELETE /api/categories/{id}")
    void deleteCategory_shouldReturnNoContent()
            throws Exception {

        doNothing()
                .when(service)
                .deleteCategory(1L);

        mockMvc.perform(
                delete("/api/categories/1"))

                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST validation failure")
    void createCategory_shouldReturnBadRequest()
            throws Exception {

        CategoryRequestDTO request =
                new CategoryRequestDTO(
                        "",
                        "");

        mockMvc.perform(
                post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isBadRequest())

                .andExpect(jsonPath("$.status")
                        .value(400));
    }

    @Test
    @DisplayName("GET category not found")
    void getCategoryById_shouldReturnNotFound()
            throws Exception {

        when(service.getCategoryById(100L))
                .thenThrow(new CategoryNotFoundException(100L));

        mockMvc.perform(
                get("/api/categories/100"))

                .andExpect(status().isNotFound())

                .andExpect(jsonPath("$.status")
                        .value(404))

                .andExpect(jsonPath("$.message")
                        .value("Category not found with id 100"));
    }
}