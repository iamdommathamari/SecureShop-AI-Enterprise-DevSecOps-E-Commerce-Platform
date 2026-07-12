package com.secureshop.backend.category;

import com.secureshop.backend.dto.CategoryRequestDTO;
import com.secureshop.backend.dto.CategoryResponseDTO;
import com.secureshop.backend.dto.PagedResponse;

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
@RequestMapping("/api/categories")
@Tag(
        name = "Category Management",
        description = "APIs for managing product categories in SecureShop"
)
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get all categories",
            description = "Retrieve all categories with pagination and sorting."
    )
    @GetMapping
    public ResponseEntity<PagedResponse<CategoryResponseDTO>> getAllCategories(

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
                service.getAllCategories(pageable));
    }

    @Operation(
            summary = "Search categories",
            description = "Search categories by keyword."
    )
    @GetMapping("/search")
    public ResponseEntity<PagedResponse<CategoryResponseDTO>> searchCategories(

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
                service.searchCategories(keyword, pageable));
    }

    @Operation(
            summary = "Get category by ID",
            description = "Retrieve a category using its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category found"),
                    @ApiResponse(responseCode = "404", description = "Category not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                service.getCategoryById(id));
    }

    @Operation(
            summary = "Create category",
            description = "Create a new category.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Category created"),
                    @ApiResponse(responseCode = "400", description = "Validation failed")
            }
    )
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @Valid @RequestBody CategoryRequestDTO request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createCategory(request));
    }

    @Operation(
            summary = "Update category",
            description = "Update an existing category.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Category updated"),
                    @ApiResponse(responseCode = "404", description = "Category not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(

            @PathVariable Long id,

            @Valid @RequestBody CategoryRequestDTO request) {

        return ResponseEntity.ok(
                service.updateCategory(id, request));
    }

    @Operation(
            summary = "Delete category",
            description = "Delete a category.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Category deleted"),
                    @ApiResponse(responseCode = "404", description = "Category not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id) {

        service.deleteCategory(id);

        return ResponseEntity.noContent().build();
    }
}