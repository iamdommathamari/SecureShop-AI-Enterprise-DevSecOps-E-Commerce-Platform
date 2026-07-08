package com.secureshop.backend.mapper;

import com.secureshop.backend.dto.UserRequestDTO;
import com.secureshop.backend.dto.UserResponseDTO;
import com.secureshop.backend.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO toResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    User toEntity(UserRequestDTO dto);
}