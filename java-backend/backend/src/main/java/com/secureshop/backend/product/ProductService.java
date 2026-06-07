package com.secureshop.backend.product;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final List<Product> products = new ArrayList<>();

    public ProductService() {

        products.add(
                new Product(
                        1L,
                        "Laptop",
                        "Gaming Laptop",
                        85000.0));

        products.add(
                new Product(
                        2L,
                        "Phone",
                        "Android Phone",
                        25000.0));
    }

    public List<Product> getAllProducts() {
        return products;
    }
    public Product getProductById(Long id) {
        return products.stream()
            .filter(product -> product.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    public Product createProduct(Product product) {

        product.setId((long) (products.size() + 1));

        products.add(product);

        return product;
    }
    public Product updateProduct(Long id, Product updatedProduct) {

        for (Product product : products) {

            if (product.getId().equals(id)) {

                product.setName(updatedProduct.getName());
                product.setDescription(updatedProduct.getDescription());
                product.setPrice(updatedProduct.getPrice());

            return product;
        }
    }

    return null;
}
}
