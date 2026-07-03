package com.secureshop.backend.mapper;

import com.secureshop.backend.category.Category;
import com.secureshop.backend.dto.CategoryRequestDTO;
import com.secureshop.backend.dto.CategoryResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequestDTO dto) {

        Category category = new Category();

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());

        return category;
    }

    public CategoryResponseDTO toResponse(Category category) {

        return new CategoryResponseDTO(

                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}