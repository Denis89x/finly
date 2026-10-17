package dev.lebenkov.finly.account.entity;

import dev.lebenkov.finly.currency.entity.Currency;
import dev.lebenkov.finly.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "accounts")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(nullable = false, length = 100)
    String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "currency_code", nullable = false)
    Currency currency;

    @Column(name = "balance", nullable = false)
    BigDecimal balance;

    @Column(name = "tracking_started_at", nullable = false)
    LocalDate trackingStartedAt;

    @Column(name = "is_archived", nullable = false)
    boolean archived;

    @Column(name = "created_at", nullable = false, updatable = false)
    Instant cratedAt;
}
