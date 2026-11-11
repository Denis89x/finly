package dev.lebenkov.finly.security.auth.dto;

import java.time.Instant;

public record RegisterResponse(

        Long id,
        String username,
        String email,
        Instant createdAt
) {
}
