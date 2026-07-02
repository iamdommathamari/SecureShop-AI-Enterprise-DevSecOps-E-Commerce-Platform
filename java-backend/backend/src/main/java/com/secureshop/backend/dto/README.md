# DTO Package

This package contains data transfer objects used to communicate between the API and service layers.

## Files
- `ProductRequestDTO.java` - Request payload for product create/update operations.
- `ProductResponseDTO.java` - Response payload for product data returned by APIs.
- `PagedResponse.java` - Generic wrapper for paginated API responses.

## Behavior
- DTOs hold request and response fields, including validation annotations.
- The service and controller layers use DTOs to isolate internal entity structure from external API contracts.
