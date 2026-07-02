package com.secureshop.backend.product;

import com.secureshop.backend.dto.ProductRequestDTO;
import com.secureshop.backend.dto.ProductResponseDTO;
import com.secureshop.backend.exception.ProductNotFoundException;
import com.secureshop.backend.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.secureshop.backend.dto.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log =
            LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductServiceImpl(ProductRepository repository,
                              ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public PagedResponse<ProductResponseDTO> getAllProducts(Pageable pageable) {

        log.info(
            "Fetching products - page: {}, size: {}, sort: {}",
            pageable.getPageNumber(),
            pageable.getPageSize(),
            pageable.getSort()
        );

        Page<ProductResponseDTO> page = repository
            .findAll(pageable)
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
    public ProductResponseDTO createProduct(ProductRequestDTO request) {

        log.info("Creating product: {}", request.getName());

        Product product = mapper.toEntity(request);

        Product saved = repository.save(product);

        log.info("Product created with id {}", saved.getId());

        return mapper.toResponse(saved);
    }

    @Override
    public ProductResponseDTO updateProduct(Long id,
                                            ProductRequestDTO request) {

        log.info("Updating product {}", id);

        Product product = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn("Product not found {}", id);

                    return new ProductNotFoundException(id);
                });

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());

        Product saved = repository.save(product);

        log.info("Product {} updated successfully", id);

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
}