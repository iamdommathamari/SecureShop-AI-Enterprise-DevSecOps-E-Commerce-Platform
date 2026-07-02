package com.secureshop.backend.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secureshop.backend.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@Import(GlobalExceptionHandler.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService service;

    @Test
    @DisplayName("GET /api/categories returns all categories")
    void getAllCategories_shouldReturnAllCategories() throws Exception {
        List<CategoryResponseDTO> categories = List.of(
                new CategoryResponseDTO(1L, "Electronics", "Devices and accessories"),
                new CategoryResponseDTO(2L, "Home", "Home products")
        );

        when(service.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()" ).value(2))
                .andExpect(jsonPath("$[0].name").value("Electronics"))
                .andExpect(jsonPath("$[1].name").value("Home"));
    }

    @Test
    @DisplayName("GET /api/categories/{id} returns category")
    void getCategoryById_shouldReturnCategory() throws Exception {
        CategoryResponseDTO category = new CategoryResponseDTO(1L, "Electronics", "Devices and accessories");

        when(service.getCategoryById(1L)).thenReturn(category);

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    @DisplayName("POST /api/categories creates category")
    void createCategory_shouldReturnCreatedCategory() throws Exception {
        CategoryRequestDTO request = new CategoryRequestDTO("Electronics", "Devices and accessories");
        CategoryResponseDTO response = new CategoryResponseDTO(1L, "Electronics", "Devices and accessories");

        when(service.createCategory(request)).thenReturn(response);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    @DisplayName("PUT /api/categories/{id} updates category")
    void updateCategory_shouldReturnUpdatedCategory() throws Exception {
        CategoryRequestDTO request = new CategoryRequestDTO("Electronics", "Updated description");
        CategoryResponseDTO response = new CategoryResponseDTO(1L, "Electronics", "Updated description");

        when(service.updateCategory(1L, request)).thenReturn(response);

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated description"));
    }

    @Test
    @DisplayName("DELETE /api/categories/{id}")
    void deleteCategory_shouldReturnNoContent() throws Exception {
        doNothing().when(service).deleteCategory(1L);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());
    }
}
