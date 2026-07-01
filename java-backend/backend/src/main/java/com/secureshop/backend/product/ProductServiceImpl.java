package com.secureshop.backend.product;

import com.secureshop.backend.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    
    private static final Logger log =
            LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Product> getAllProducts() {

        log.info("Fetching all products");

        return repository.findAll();
   }

   @Override
   public Product getProductById(Long id) {

        log.info("Fetching product with id {}", id);

        return repository.findById(id)
            .orElseThrow(() -> {

                log.warn("Product not found with id {}", id);

                return new ProductNotFoundException(id);
            });
    }
    
   @Override
    public Product createProduct(Product product) {

        log.info("Creating product: {}", product.getName());

        Product saved = repository.save(product);

        log.info("Product created with id {}", saved.getId());

        return saved;
    }

    @Override
    public Product updateProduct(Long id, Product updatedProduct) {

        log.info("Updating product {}", id);

        Product product = repository.findById(id)
            .orElseThrow(() -> {

                log.warn("Product not found {}", id);

                return new ProductNotFoundException(id);
            });

        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());

        Product saved = repository.save(product);

        log.info("Product {} updated successfully", id);

        return saved;
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