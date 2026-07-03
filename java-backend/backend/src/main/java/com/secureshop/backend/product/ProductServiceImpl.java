package com.secureshop.backend.product;

import com.secureshop.backend.dto.PagedResponse;
import com.secureshop.backend.dto.ProductRequestDTO;
import com.secureshop.backend.dto.ProductResponseDTO;
import com.secureshop.backend.exception.ProductNotFoundException;
import com.secureshop.backend.mapper.ProductMapper;
import com.secureshop.backend.category.Category;
import com.secureshop.backend.category.CategoryRepository;
import com.secureshop.backend.exception.CategoryNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log =
            LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper mapper;
    

    public ProductServiceImpl(
        ProductRepository repository,
        CategoryRepository categoryRepository,
        ProductMapper mapper) {

            this.repository = repository;
            this.categoryRepository = categoryRepository;
            this.mapper = mapper;
    }
    @Override
    public PagedResponse<ProductResponseDTO> getAllProducts(
            Pageable pageable) {

        log.info(
                "Fetching products - page: {}, size: {}, sort: {}",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort());

        Page<ProductResponseDTO> page = repository
                .findAll(pageable)
                .map(mapper::toResponse);

        return PagedResponse.from(page);
    }

    @Override
    public PagedResponse<ProductResponseDTO> searchProducts(
            String keyword,
            Pageable pageable) {

        log.info("Searching products with keyword: {}", keyword);

        Page<ProductResponseDTO> page = repository
                .findByNameContainingIgnoreCase(keyword, pageable)
                .map(mapper::toResponse);

        return PagedResponse.from(page);
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {

        log.info("Fetching product with id {}", id);

        Product product = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Product not found with id {}", id);

                    return new ProductNotFoundException(id);
                });

        return mapper.toResponse(product);
    }

    @Override
public ProductResponseDTO createProduct(
        ProductRequestDTO request) {

    log.info("Creating product: {}", request.getName());

    Category category = categoryRepository
            .findById(request.getCategoryId())
            .orElseThrow(() -> {

                log.warn(
                        "Category not found with id {}",
                        request.getCategoryId());

                return new CategoryNotFoundException(
                        request.getCategoryId());
            });

    Product product = mapper.toEntity(request);

    product.setCategory(category);

    Product saved = repository.save(product);

    log.info(
            "Product created successfully with id {}",
            saved.getId());

    return mapper.toResponse(saved);
}

    @Override
public ProductResponseDTO updateProduct(
        Long id,
        ProductRequestDTO request) {

    log.info("Updating product {}", id);

    Product product = repository.findById(id)
            .orElseThrow(() -> {

                log.warn(
                        "Product not found with id {}",
                        id);

                return new ProductNotFoundException(id);
            });

    Category category = categoryRepository
            .findById(request.getCategoryId())
            .orElseThrow(() -> {

                log.warn(
                        "Category not found with id {}",
                        request.getCategoryId());

                return new CategoryNotFoundException(
                        request.getCategoryId());
            });

    product.setName(request.getName());
    product.setDescription(request.getDescription());
    product.setPrice(request.getPrice());
    product.setCategory(category);

    Product saved = repository.save(product);

    log.info(
            "Product {} updated successfully",
            id);

    return mapper.toResponse(saved);
}

    @Override
    public void deleteProduct(Long id) {

        log.info("Deleting product {}", id);

        Product product = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Product not found {}", id);

                    return new ProductNotFoundException(id);
                });

        repository.delete(product);

        log.info("Product {} deleted successfully", id);
    }
    @Override
public PagedResponse<ProductResponseDTO> getProductsByCategory(
        Long categoryId,
        Pageable pageable) {

    log.info(
            "Fetching products for category {}",
            categoryId);

    Page<ProductResponseDTO> page = repository
            .findByCategoryId(categoryId, pageable)
            .map(mapper::toResponse);

    return PagedResponse.from(page);
}
}