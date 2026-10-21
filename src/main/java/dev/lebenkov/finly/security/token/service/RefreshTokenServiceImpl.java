package dev.lebenkov.finly.security.token.service;

import dev.lebenkov.finly.security.token.entity.RefreshToken;
import dev.lebenkov.finly.security.token.repository.RefreshTokenRepository;
import dev.lebenkov.finly.user.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {

    RefreshTokenRepository refreshTokenRepository;

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(String token) {
        return refreshTokenRepository.findByTokenHash(hashToken(token))
                .filter(refreshToken -> refreshToken.getRevokedAt() == null)
                .filter(refreshToken -> refreshToken.getExpiresAt().isAfter(Instant.now()))
                .isPresent();
    }

    @Override
    @Transactional
    public void revoke(String token) {
        refreshTokenRepository.findByTokenHash(hashToken(token))
                .ifPresent(RefreshToken::revoke);
    }

    @Override
    @Transactional
    public void save(String token, User user, Instant expiresAt) {
        RefreshToken refreshToken = new RefreshToken(
                user, hashToken(token), expiresAt
        );

        refreshTokenRepository.save(refreshToken);
    }
}
