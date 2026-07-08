package com.secureshop.backend.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("save user")
    void saveUser_shouldPersist() {

        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .password("password")
                .role(Role.ROLE_CUSTOMER)
                .build();

        User saved = repository.save(user);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail())
                .isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("findByEmail")
    void findByEmail_shouldReturnUser() {

        User user = repository.save(
                User.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .email("john@example.com")
                        .password("password")
                        .role(Role.ROLE_CUSTOMER)
                        .build());

        Optional<User> result =
                repository.findByEmail("john@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getId())
                .isEqualTo(user.getId());
    }

    @Test
    @DisplayName("existsByEmail")
    void existsByEmail_shouldReturnTrue() {

        repository.save(
                User.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .email("john@example.com")
                        .password("password")
                        .role(Role.ROLE_CUSTOMER)
                        .build());

        assertThat(
                repository.existsByEmail("john@example.com"))
                .isTrue();

        assertThat(
                repository.existsByEmail("unknown@example.com"))
                .isFalse();
    }
}