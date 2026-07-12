package com.secureshop.backend.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secureshop.backend.auth.LoginRequest;
import com.secureshop.backend.auth.LoginResponse;
import com.secureshop.backend.auth.RegisterRequest;
import com.secureshop.backend.auth.RegisterResponse;
import com.secureshop.backend.security.JwtAuthenticationEntryPoint;
import com.secureshop.backend.security.JwtAuthenticationFilter;
import com.secureshop.backend.security.JwtService;
import com.secureshop.backend.security.UserDetailsServiceImpl;
import com.secureshop.backend.auth.AuthController;
import com.secureshop.backend.auth.AuthenticationService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
private JwtAuthenticationFilter jwtAuthenticationFilter;

@MockitoBean
private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

@MockitoBean
private UserDetailsServiceImpl userDetailsService;

@MockitoBean
private JwtService jwtService;

    @Test
    @DisplayName("Register should return 201 Created")
    void register_shouldReturnCreated() throws Exception {

        RegisterRequest request =
                new RegisterRequest(
                        "John",
                        "Doe",
                        "john@example.com",
                        "password123");

        RegisterResponse response =
                RegisterResponse.builder()
                        .id(1L)
                        .firstName("John")
                        .lastName("Doe")
                        .email("john@example.com")
                        .role("ROLE_CUSTOMER")
                        .message("Registration successful")
                        .build();

        given(authenticationService.register(any(RegisterRequest.class)))
                .willReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.role").value("ROLE_CUSTOMER"));
    }

    @Test
    @DisplayName("Register should return 400 for invalid request")
    void register_shouldReturnBadRequest() throws Exception {

        RegisterRequest request =
                new RegisterRequest(
                        "",
                        "",
                        "invalid-email",
                        "");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Login should return JWT")
    void login_shouldReturnJwtToken() throws Exception {

        LoginRequest request =
                new LoginRequest(
                        "john@example.com",
                        "password123");

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

        given(authenticationService.login(any(LoginRequest.class)))
                .willReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @DisplayName("Login should return 400 for invalid request")
    void login_shouldReturnBadRequest() throws Exception {

        LoginRequest request =
                new LoginRequest(
                        "",
                        "");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}