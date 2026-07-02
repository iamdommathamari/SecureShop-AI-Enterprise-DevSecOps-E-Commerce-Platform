package com.secureshop.backend.product;

import com.secureshop.backend.dto.PagedResponse;
import com.secureshop.backend.dto.ProductRequestDTO;
import com.secureshop.backend.dto.ProductResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@Tag(
        name = "Product Management",
        description = "APIs for managing products in SecureShop"
)
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get all products",
            description = "Retrieve all available products."
    )
    @GetMapping
    public ResponseEntity<PagedResponse<ProductResponseDTO>> getAllProducts(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "id")
            String sort,

            @RequestParam(defaultValue = "asc")
            String direction) {

        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(sortDirection, sort));

        return ResponseEntity.ok(
                service.getAllProducts(pageable));
    }

    @Operation(
            summary = "Search products",
            description = "Search products by keyword."
    )
    @GetMapping("/search")
    public ResponseEntity<PagedResponse<ProductResponseDTO>> searchProducts(

            @RequestParam
            String keyword,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "id")
            String sort,

            @RequestParam(defaultValue = "asc")
            String direction) {

        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(sortDirection, sort));

        return ResponseEntity.ok(
                service.searchProducts(keyword, pageable));
    }

    @Operation(
            summary = "Get product by ID",
            description = "Retrieve a product using its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product found"),
                    @ApiResponse(responseCode = "404", description = "Product not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getProductById(id));
    }

    @Operation(
            summary = "Create a new product",
            description = "Create a new product.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Product created"),
                    @ApiResponse(responseCode = "400", description = "Validation failed")
            }
    )
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Valid @RequestBody ProductRequestDTO request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createProduct(request));
    }

    @Operation(
            summary = "Update product",
            description = "Update an existing product."
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(

            @PathVariable Long id,

            @Valid @RequestBody ProductRequestDTO request) {

        return ResponseEntity.ok(
                service.updateProduct(id, request));
    }

    @Operation(
            summary = "Delete product",
            description = "Delete a product."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id) {

        service.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
}