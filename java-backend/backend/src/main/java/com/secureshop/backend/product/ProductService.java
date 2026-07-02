package com.secureshop.backend.product;

import com.secureshop.backend.dto.PagedResponse;
import com.secureshop.backend.dto.ProductRequestDTO;
import com.secureshop.backend.dto.ProductResponseDTO;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    PagedResponse<ProductResponseDTO> getAllProducts(
            Pageable pageable);

    PagedResponse<ProductResponseDTO> searchProducts(
            String keyword,
            Pageable pageable);

    ProductResponseDTO getProductById(
            Long id);

    ProductResponseDTO createProduct(
            ProductRequestDTO request);

    ProductResponseDTO updateProduct(
            Long id,
            ProductRequestDTO request);

    void deleteProduct(
            Long id);
}