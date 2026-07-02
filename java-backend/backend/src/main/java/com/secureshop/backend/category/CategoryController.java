package com.secureshop.backend.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category Management", description = "APIs for managing product categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @Operation(summary = "Get all categories", description = "Retrieve all product categories")
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(service.getAllCategories());
    }

    @Operation(summary = "Get category by ID", description = "Retrieve a category by its ID",
            responses = {@ApiResponse(responseCode = "200", description = "Category found"),
                    @ApiResponse(responseCode = "404", description = "Category not found")})
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getCategoryById(id));
    }

    @Operation(summary = "Create a new category", description = "Create a new product category",
            responses = {@ApiResponse(responseCode = "201", description = "Category created successfully"),
                    @ApiResponse(responseCode = "400", description = "Validation failed")})
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createCategory(request));
    }

    @Operation(summary = "Update a category", description = "Update an existing category",
            responses = {@ApiResponse(responseCode = "200", description = "Category updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Validation failed"),
                    @ApiResponse(responseCode = "404", description = "Category not found")})
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Long id,
                                                              @Valid @RequestBody CategoryRequestDTO request) {
        return ResponseEntity.ok(service.updateCategory(id, request));
    }

    @Operation(summary = "Delete a category", description = "Delete a product category",
            responses = {@ApiResponse(responseCode = "204", description = "Category deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Category not found")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        service.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
