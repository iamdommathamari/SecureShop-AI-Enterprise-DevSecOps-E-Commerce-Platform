# Mapper Package

This package contains mapper classes that translate between domain entities and DTOs.

## Files
- `ProductMapper.java` - Converts `ProductRequestDTO` to `Product` and `Product` to `ProductResponseDTO`.

## Behavior
- Keeps entity-to-DTO conversion logic centralized.
- Ensures API responses contain category identifiers without exposing full entity relations.
