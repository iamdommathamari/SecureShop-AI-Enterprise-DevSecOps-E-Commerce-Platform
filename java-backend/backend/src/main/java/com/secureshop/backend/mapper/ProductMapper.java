package com.secureshop.backend.mapper;

import com.secureshop.backend.dto.ProductRequestDTO;
import com.secureshop.backend.dto.ProductResponseDTO;
import com.secureshop.backend.product.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponseDTO toResponse(Product product) {

        if (product == null) {
            return null;
        }

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }

    public Product toEntity(ProductRequestDTO request) {

        if (request == null) {
            return null;
        }

        return new Product(
                null,
                request.getName(),
                request.getDescription(),
                request.getPrice()
        );
    }
}