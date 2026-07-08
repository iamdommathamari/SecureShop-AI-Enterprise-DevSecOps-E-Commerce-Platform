package com.secureshop.backend.user;

import com.secureshop.backend.dto.PagedResponse;
import com.secureshop.backend.dto.UserRequestDTO;
import com.secureshop.backend.dto.UserResponseDTO;
import com.secureshop.backend.exception.UserAlreadyExistsException;
import com.secureshop.backend.exception.UserNotFoundException;
import com.secureshop.backend.mapper.UserMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl service;

    private User user;
    private UserRequestDTO request;
    private UserResponseDTO response;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .password("encodedPassword")
                .role(Role.ROLE_CUSTOMER)
                .enabled(true)
                .build();

        request = new UserRequestDTO(
                "John",
                "Doe",
                "john@example.com",
                "password123");

        response = new UserResponseDTO(
                1L,
                "John",
                "Doe",
                "john@example.com",
                Role.ROLE_CUSTOMER,
                true);
    }

    @Test
    @DisplayName("getAllUsers")
    void getAllUsers_shouldReturnPagedUsers() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<User> page =
                new PageImpl<>(List.of(user));

        when(repository.findAll(pageable))
                .thenReturn(page);

        when(mapper.toResponse(user))
                .thenReturn(response);

        PagedResponse<UserResponseDTO> result =
                service.getAllUsers(pageable);

        assertThat(result.getContent()).hasSize(1);

        verify(repository).findAll(pageable);
    }

    @Test
    @DisplayName("getUserById")
    void getUserById_shouldReturnUser() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(user));

        when(mapper.toResponse(user))
                .thenReturn(response);

        UserResponseDTO result =
                service.getUserById(1L);

        assertThat(result.getEmail())
                .isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("getUserById not found")
    void getUserById_shouldThrowException() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.getUserById(1L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("getUserByEmail")
    void getUserByEmail_shouldReturnUser() {

        when(repository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(mapper.toResponse(user))
                .thenReturn(response);

        UserResponseDTO result =
                service.getUserByEmail(user.getEmail());

        assertThat(result.getEmail())
                .isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("createUser")
    void createUser_shouldSaveUser() {

        when(repository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(mapper.toEntity(request))
                .thenReturn(user);

        when(passwordEncoder.encode(request.getPassword()))
                .thenReturn("encodedPassword");

        when(repository.save(any(User.class)))
                .thenReturn(user);

        when(mapper.toResponse(user))
                .thenReturn(response);

        UserResponseDTO result =
                service.createUser(request);

        assertThat(result.getEmail())
                .isEqualTo(request.getEmail());

        verify(repository).save(any(User.class));
    }

    @Test
    @DisplayName("createUser duplicate email")
    void createUser_shouldThrowDuplicateEmailException() {

        when(repository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        assertThatThrownBy(() ->
                service.createUser(request))
                .isInstanceOf(UserAlreadyExistsException.class);

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("updateUser")
    void updateUser_shouldUpdateUser() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(user));

        when(repository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(passwordEncoder.encode(request.getPassword()))
                .thenReturn("encodedPassword");

        when(repository.save(any(User.class)))
                .thenReturn(user);

        when(mapper.toResponse(user))
                .thenReturn(response);

        UserResponseDTO result =
                service.updateUser(1L, request);

        assertThat(result.getFirstName())
                .isEqualTo("John");

        verify(repository).save(any(User.class));
    }

    @Test
    @DisplayName("deleteUser")
    void deleteUser_shouldDeleteUser() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(user));

        service.deleteUser(1L);

        verify(repository).delete(user);
    }
}