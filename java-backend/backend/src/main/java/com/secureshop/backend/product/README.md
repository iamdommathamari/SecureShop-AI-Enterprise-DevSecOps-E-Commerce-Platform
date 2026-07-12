# Product Module

This package contains the product domain for SecureShop.

## Files
- `Product.java` - JPA entity representing a product, including a many-to-one relationship with `Category`.
- `ProductController.java` - REST controller exposing `/api/products` endpoints.
- `ProductService.java` - Service contract for product business operations.
- `ProductServiceImpl.java` - Concrete service implementation that handles product creation, update, retrieval, deletion, and pagination.
- `ProductRepository.java` - Spring Data JPA repository for product persistence.

## Behavior
- Supports create, read, update, and delete operations for products.
- Uses category lookup by ID for product create/update flows.
- Returns paginated product lists via `PagedResponse`.
