package com.secureshop.backend.category;

import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequestDTO dto) {
        return new Category(
                null,
                dto.getName(),
                dto.getDescription()
        );
    }

    public CategoryResponseDTO toResponse(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}
