# SecureShop Backend Module

This folder contains the backend service for SecureShop, built with Spring Boot and Java 21.

## Purpose
- Hosts the complete backend application, including the main Spring Boot launcher, configuration, domain logic, API controllers, DTOs, exception handling, and tests.
- Supports product and category management, REST API endpoints, validation, and pagination.

## Important files
- `pom.xml` - Maven build file with Spring Boot, JPA, PostgreSQL, testing, and OpenAPI dependencies.
- `mvnw`, `mvnw.cmd` - Maven wrapper scripts for consistent build execution on Windows and Unix.
- `HELP.md` - Helpful project information and commands for package management and running tests.

## Key folders
- `src/main/java` - Main application source code.
- `src/main/resources` - Static resources, templates, and application properties.
- `src/test/java` - Unit and slice tests for controllers, services, and repositories.
- `target` - Build output produced by Maven.

## How to run
- Build: `./mvnw clean package`
- Run tests: `./mvnw test`
- Start service: `./mvnw spring-boot:run`
