package com.secureshop.backend.dto;

public record CartResponseDTO(

        Long id,

        Long productId,

        String productName,

        Double price,

        Integer quantity,

        Double subtotal
) {
}