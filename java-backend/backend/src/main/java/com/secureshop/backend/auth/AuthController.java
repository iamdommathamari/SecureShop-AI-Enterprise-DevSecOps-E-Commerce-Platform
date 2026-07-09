package com.secureshop.backend.auth;

import com.secureshop.backend.security.UserDetailsImpl;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        RegisterResponse response =
                authenticationService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response =
                authenticationService.login(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<RegisterResponse> me(
            Authentication authentication) {

        UserDetailsImpl user =
                (UserDetailsImpl) authentication.getPrincipal();

        RegisterResponse response =
                RegisterResponse.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getUsername())
                        .role(
                                user.getAuthorities()
                                        .iterator()
                                        .next()
                                        .getAuthority())
                        .message("Authenticated user")
                        .build();

        return ResponseEntity.ok(response);
    }
}