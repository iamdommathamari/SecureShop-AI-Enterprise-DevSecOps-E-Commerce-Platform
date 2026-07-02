package com.secureshop.backend.mapper;

import com.secureshop.backend.dto.ProductRequestDTO;
import com.secureshop.backend.dto.ProductResponseDTO;
import com.secureshop.backend.product.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDTO dto) {

        return new Product(
            null,
            dto.getName(),
            dto.getDescription(),
            dto.getPrice(),
            null
        );
    }

    public ProductResponseDTO toResponse(Product product) {

        Long categoryId = product.getCategory() != null
                ? product.getCategory().getId()
                : null;

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                categoryId);
    }
}