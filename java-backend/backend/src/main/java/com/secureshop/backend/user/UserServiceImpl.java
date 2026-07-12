package com.secureshop.backend.user;

import com.secureshop.backend.dto.PagedResponse;
import com.secureshop.backend.dto.UserRequestDTO;
import com.secureshop.backend.dto.UserResponseDTO;
import com.secureshop.backend.exception.UserAlreadyExistsException;
import com.secureshop.backend.exception.UserNotFoundException;
import com.secureshop.backend.mapper.UserMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log =
            LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository repository,
            UserMapper mapper,
            PasswordEncoder passwordEncoder) {

        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PagedResponse<UserResponseDTO> getAllUsers(
            Pageable pageable) {

        log.info(
                "Fetching users - page: {}, size: {}, sort: {}",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort());

        Page<UserResponseDTO> page = repository
                .findAll(pageable)
                .map(mapper::toResponse);

        return PagedResponse.from(page);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {

        log.info("Fetching user with id {}", id);

        User user = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn("User not found with id {}", id);

                    return new UserNotFoundException(id);
                });

        return mapper.toResponse(user);
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {

        log.info("Fetching user with email {}", email);

        User user = repository.findByEmail(email)
                .orElseThrow(() -> {

                    log.warn("User not found with email {}", email);

                    return new UserNotFoundException(email);
                });

        return mapper.toResponse(user);
    }

    @Override
    public UserResponseDTO createUser(
            UserRequestDTO request) {

        log.info("Creating user {}", request.getEmail());

        if (repository.existsByEmail(request.getEmail())) {

            log.warn("Email already exists {}", request.getEmail());

            throw new UserAlreadyExistsException(
                    request.getEmail());
        }

        User user = mapper.toEntity(request);

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()));

        user.setRole(Role.ROLE_CUSTOMER);

        user.setEnabled(true);

        User saved = repository.save(user);

        log.info(
                "User created successfully with id {}",
                saved.getId());

        return mapper.toResponse(saved);
    }

    @Override
    public UserResponseDTO updateUser(
            Long id,
            UserRequestDTO request) {

        log.info("Updating user {}", id);

        User user = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn("User not found {}", id);

                    return new UserNotFoundException(id);
                });

        if (!user.getEmail().equals(request.getEmail())
                && repository.existsByEmail(request.getEmail())) {

            log.warn(
                    "Email already exists {}",
                    request.getEmail());

            throw new UserAlreadyExistsException(
                    request.getEmail());
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()));

        User saved = repository.save(user);

        log.info(
                "User {} updated successfully",
                id);

        return mapper.toResponse(saved);
    }

    @Override
    public void deleteUser(Long id) {

        log.info("Deleting user {}", id);

        User user = repository.findById(id)
                .orElseThrow(() -> {

                    log.warn("User not found {}", id);

                    return new UserNotFoundException(id);
                });

        repository.delete(user);

        log.info("User {} deleted successfully", id);
    }
}