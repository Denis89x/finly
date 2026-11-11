package dev.lebenkov.finly.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtService {

    static final String TOKEN_TYPE_CLAIM = "token_type";
    static final String ACCESS = "ACCESS";
    static final String REFRESH = "REFRESH";

    final SecretKey secretKey;
    final long accessExpiration;
    final long refreshExpiration;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-expiration}") long accessExpiration,
            @Value("${jwt.refresh-expiration}") long refreshExpiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(secret)
        );
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    public GeneratedToken generateAccessToken(String username) {
        return generateToken(username, ACCESS, accessExpiration);
    }

    public GeneratedToken generateRefreshToken(String username) {
        return generateToken(username, REFRESH, refreshExpiration);
    }

    public boolean isAccessTokenValid(String token, String username) {
        return isTokenValid(token, username, ACCESS);
    }

    public boolean isRefreshTokenValid(String token, String username) {
        return isTokenValid(token, username, REFRESH);
    }

    private GeneratedToken generateToken(
            String username,
            String tokenType,
            long expiration
    ) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusMillis(expiration);

        String token = Jwts.builder()
                .subject(username)
                .claim(TOKEN_TYPE_CLAIM, tokenType)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(secretKey)
                .compact();

        return new GeneratedToken(token, expiresAt);
    }

    public String extractUsernameFromAccessToken(String token) {
        Claims claims = extractClaims(token);

        if (!ACCESS.equals(claims.get(TOKEN_TYPE_CLAIM, String.class))) {
            throw new JwtException("Invalid token type");
        }

        return claims.getSubject();
    }

    private boolean isTokenValid(
            String token,
            String username,
            String expectType
    ) {
        try {
            Claims claims = extractClaims(token);

            return claims.getSubject().equals(username)
                    && expectType.equals(
                    claims.get(TOKEN_TYPE_CLAIM, String.class)
            );
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}