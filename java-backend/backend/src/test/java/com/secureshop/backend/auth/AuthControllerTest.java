package com.secureshop.backend.auth;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.secureshop.backend.security.SecurityConfig;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.context.annotation.Import;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthenticationService authenticationService;

    @Test
    @DisplayName("Register Endpoint")
    void register_shouldReturnCreated()
            throws Exception {

        RegisterRequest request =
                RegisterRequest.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .email("john@example.com")
                        .password("password123")
                        .build();

        RegisterResponse response =
                RegisterResponse.builder()
                        .id(1L)
                        .firstName("John")
                        .lastName("Doe")
                        .email("john@example.com")
                        .role("ROLE_CUSTOMER")
                        .message("Registration successful")
                        .build();

        when(authenticationService.register(any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.email")
                        .value("john@example.com"))

                .andExpect(jsonPath("$.role")
                        .value("ROLE_CUSTOMER"));
    }

    @Test
    @DisplayName("Login Endpoint")
    void login_shouldReturnToken()
            throws Exception {

        LoginRequest request =
                LoginRequest.builder()
                        .email("john@example.com")
                        .password("password123")
                        .build();

        LoginResponse response =
                LoginResponse.builder()
                        .token("jwt-token")
                        .tokenType("Bearer")
                        .userId(1L)
                        .firstName("John")
                        .lastName("Doe")
                        .email("john@example.com")
                        .role("ROLE_CUSTOMER")
                        .build();

        when(authenticationService.login(any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.token")
                        .value("jwt-token"))

                .andExpect(jsonPath("$.email")
                        .value("john@example.com"));
    }

}