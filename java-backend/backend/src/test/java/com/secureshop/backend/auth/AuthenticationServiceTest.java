package com.secureshop.backend.auth;

import com.secureshop.backend.security.JwtService;
import com.secureshop.backend.security.UserDetailsImpl;
import com.secureshop.backend.user.Role;
import com.secureshop.backend.user.User;
import com.secureshop.backend.user.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthenticationServiceImpl service;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {

        registerRequest = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .password("password123")
                .build();

        loginRequest = LoginRequest.builder()
                .email("john@example.com")
                .password("password123")
                .build();

        user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .password("encodedPassword")
                .role(Role.ROLE_CUSTOMER)
                .enabled(true)
                .build();

        userDetails = new UserDetailsImpl(user);
    }

    @Test
    @DisplayName("Register User")
    void register_shouldCreateUser() {

        when(userRepository.existsByEmail(registerRequest.getEmail()))
                .thenReturn(false);

        when(passwordEncoder.encode(registerRequest.getPassword()))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        RegisterResponse response =
                service.register(registerRequest);

        assertThat(response.getEmail())
                .isEqualTo(user.getEmail());

        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Register Duplicate Email")
    void register_shouldThrowException() {

        when(userRepository.existsByEmail(registerRequest.getEmail()))
                .thenReturn(true);

        assertThatThrownBy(() ->
                service.register(registerRequest))
                .isInstanceOf(RuntimeException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Login")
    void login_shouldReturnJwtToken() {

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        when(authentication.getPrincipal())
                .thenReturn(userDetails);

        when(jwtService.generateToken(userDetails))
                .thenReturn("jwt-token");

        LoginResponse response =
                service.login(loginRequest);

        assertThat(response.getToken())
                .isEqualTo("jwt-token");

        assertThat(response.getEmail())
                .isEqualTo("john@example.com");

        verify(jwtService).generateToken(userDetails);
    }

}