# Category Module

This package manages the category domain for SecureShop.

## Files
- `Category.java` - JPA entity representing a product category.
- `CategoryController.java` - REST controller exposing `/api/categories` endpoints.
- `CategoryService.java` - Service contract for category operations.
- `CategoryServiceImpl.java` - Implementation of category business logic.
- `CategoryRepository.java` - Spring Data JPA repository for category persistence.
- `CategoryMapper.java` - Converts between category entities and DTOs.
- `CategoryRequestDTO.java` - DTO for category creation and update requests.
- `CategoryResponseDTO.java` - DTO returned in API responses.

## Behavior
- Supports create, read, update, and delete operations for categories.
- Uses validation annotations to enforce required fields.
- Integrates with the service layer and repository for persistence.
