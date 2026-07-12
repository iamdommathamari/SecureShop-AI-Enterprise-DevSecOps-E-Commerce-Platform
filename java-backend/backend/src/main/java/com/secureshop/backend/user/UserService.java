package com.secureshop.backend.user;

import org.springframework.data.domain.Pageable;

import com.secureshop.backend.dto.PagedResponse;
import com.secureshop.backend.dto.UserRequestDTO;
import com.secureshop.backend.dto.UserResponseDTO;

public interface UserService {

    PagedResponse<UserResponseDTO> getAllUsers(
            Pageable pageable);

    UserResponseDTO getUserById(Long id);

    UserResponseDTO getUserByEmail(String email);

    UserResponseDTO createUser(
            UserRequestDTO request);

    UserResponseDTO updateUser(
            Long id,
            UserRequestDTO request);

    void deleteUser(Long id);
}