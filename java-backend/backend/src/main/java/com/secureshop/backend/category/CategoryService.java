package com.secureshop.backend.category;

import com.secureshop.backend.dto.CategoryRequestDTO;
import com.secureshop.backend.dto.CategoryResponseDTO;
import com.secureshop.backend.dto.PagedResponse;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    PagedResponse<CategoryResponseDTO> getAllCategories(
            Pageable pageable);

    PagedResponse<CategoryResponseDTO> searchCategories(
            String keyword,
            Pageable pageable);

    CategoryResponseDTO getCategoryById(
            Long id);

    CategoryResponseDTO createCategory(
            CategoryRequestDTO request);

    CategoryResponseDTO updateCategory(
            Long id,
            CategoryRequestDTO request);

    void deleteCategory(
            Long id);
}