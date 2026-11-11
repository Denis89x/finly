package dev.lebenkov.finly.security.jwt;

import java.time.Instant;

public record GeneratedToken(
        String value,
        Instant expiresAt
) {
}
