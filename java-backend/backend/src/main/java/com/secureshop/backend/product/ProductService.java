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
}
