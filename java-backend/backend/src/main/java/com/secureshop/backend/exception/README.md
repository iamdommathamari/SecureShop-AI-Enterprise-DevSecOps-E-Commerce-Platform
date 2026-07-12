# Exception Package

This package defines custom exceptions and global exception handling for the backend.

## Files
- `ProductNotFoundException.java` - Raised when a product is not found.
- `CategoryNotFoundException.java` - Raised when a category is not found.
- `ErrorResponse.java` - Standard error response payload for API failures.
- `GlobalExceptionHandler.java` - Centralized handling for validation and not-found exceptions.

## Behavior
- Converts exceptions to structured HTTP responses.
- Ensures consistent error payloads for client-facing APIs.
