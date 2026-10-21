package dev.lebenkov.finly.security.token.service;

import dev.lebenkov.finly.user.entity.User;

import java.time.Instant;

public interface RefreshTokenService {

    void save(String token, User user, Instant expiresAt);

    boolean isValid(String token);

    void revoke(String token);
}
