package com.secureshop.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String role;
}