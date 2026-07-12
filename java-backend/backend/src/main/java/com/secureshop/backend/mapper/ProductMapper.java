package com.secureshop.backend.mapper;

import com.secureshop.backend.dto.ProductRequestDTO;
import com.secureshop.backend.dto.ProductResponseDTO;
import com.secureshop.backend.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    ProductResponseDTO toResponse(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductRequestDTO dto);
}