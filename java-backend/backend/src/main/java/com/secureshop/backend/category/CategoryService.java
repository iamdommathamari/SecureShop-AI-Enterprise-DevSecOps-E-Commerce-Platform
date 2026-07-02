package com.secureshop.backend.category;

import java.util.List;

public interface CategoryService {

    List<CategoryResponseDTO> getAllCategories();

    CategoryResponseDTO getCategoryById(Long id);

    CategoryResponseDTO createCategory(CategoryRequestDTO request);

    CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO request);

    void deleteCategory(Long id);
}
