package com.secureshop.backend.auth;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.secureshop.backend.security.UserDetailsImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import com.secureshop.backend.config.SecurityConfig;

import org.springframework.http.MediaType;

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    @DisplayName("POST /api/auth/register")
    void register_shouldReturnCreated()
            throws Exception {

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

        when(authenticationService.register(any(RegisterRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email")
                        .value("john@example.com"))
                .andExpect(jsonPath("$.role")
                        .value("ROLE_CUSTOMER"));
    }

    @Test
    @DisplayName("POST /api/auth/login")
    void login_shouldReturnJwtToken()
            throws Exception {

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

        when(authenticationService.login(any(LoginRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token")
                        .value("jwt-token"))
                .andExpect(jsonPath("$.tokenType")
                        .value("Bearer"));
    }

    @Test
    @WithMockUser(username = "john@example.com")
    @DisplayName("GET /api/auth/me")
    void me_shouldReturnAuthenticatedUser()
            throws Exception {

        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST register validation")
    void register_shouldReturnBadRequest()
            throws Exception {

        RegisterRequest request =
                new RegisterRequest(
                        "",
                        "",
                        "",
                        "");

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST login validation")
    void login_shouldReturnBadRequest()
            throws Exception {

        LoginRequest request =
                new LoginRequest(
                        "",
                        "");

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}