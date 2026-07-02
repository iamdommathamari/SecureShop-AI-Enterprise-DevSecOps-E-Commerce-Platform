# Product Test Package

This folder contains tests for the product module.

## Files
- `ProductServiceTest.java` - Unit tests for product service behavior, including pagination and category association.
- `ProductControllerTest.java` - REST controller slice tests for `/api/products` endpoints.
- `ProductRepositoryTest.java` - Repository-level tests for JPA persistence and query behavior.

## Behavior
- Ensures product operations succeed and return correct DTOs.
- Verifies error handling for not-found products and validation failures.
