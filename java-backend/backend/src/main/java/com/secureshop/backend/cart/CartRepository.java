package com.secureshop.backend.cart;

import com.secureshop.backend.product.Product;
import com.secureshop.backend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository
        extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUser(User user);

    Optional<CartItem> findByUserAndProduct(
            User user,
            Product product);

    void deleteByUser(User user);

    void deleteByUserAndProduct(
            User user,
            Product product);
}