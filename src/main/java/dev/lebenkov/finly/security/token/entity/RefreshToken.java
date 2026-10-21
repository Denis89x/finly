package dev.lebenkov.finly.security.token.entity;

import dev.lebenkov.finly.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "refresh_token")
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "token_hash", nullable = false, unique = true, length = 64)
    String tokenHash;

    @Column(name = "expires_at", nullable = false)
    Instant expiresAt;

    @Column(name = "revoked_at")
    Instant revokedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    Instant createdAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }

    public void revoke() {
        this.revokedAt = Instant.now();
    }

    public RefreshToken(User user, String tokenHash, Instant expiresAt) {
        this.user = user;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
    }
}
