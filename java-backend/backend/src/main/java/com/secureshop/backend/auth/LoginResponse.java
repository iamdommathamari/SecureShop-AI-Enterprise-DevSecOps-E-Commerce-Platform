package com.secureshop.backend.auth;

import java.time.Instant;

public record LoginResponse(

        String accessToken,

        String tokenType,

        Long expiresIn,

        Instant issuedAt
) {
}