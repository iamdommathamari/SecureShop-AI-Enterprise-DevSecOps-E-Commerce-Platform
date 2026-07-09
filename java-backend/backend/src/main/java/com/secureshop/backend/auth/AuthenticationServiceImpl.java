package com.secureshop.backend.auth;

import com.secureshop.backend.exception.UserAlreadyExistsException;
import com.secureshop.backend.security.JwtService;
import com.secureshop.backend.security.UserDetailsImpl;
import com.secureshop.backend.user.Role;
import com.secureshop.backend.user.User;
import com.secureshop.backend.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Override
    public RegisterResponse register(RegisterRequest request) {

        log.info("Registering user {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()))
                .role(Role.ROLE_CUSTOMER)
                .enabled(true)
                .build();

        User saved = userRepository.save(user);

        log.info("User registered successfully {}", saved.getEmail());

        return RegisterResponse.builder()
                .id(saved.getId())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .email(saved.getEmail())
                .role(saved.getRole().name())
                .message("Registration successful")
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        log.info("Authenticating {}", request.getEmail());

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()));

        UserDetailsImpl user =
                (UserDetailsImpl) authentication.getPrincipal();

        String token =
                jwtService.generateToken(user);

        return LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getUsername())
                .role(
                        user.getAuthorities()
                                .iterator()
                                .next()
                                .getAuthority())
                .build();
    }
}