package dev.lebenkov.finly.user.service;

import dev.lebenkov.finly.security.auth.dto.RegisterRequest;
import dev.lebenkov.finly.user.entity.User;
import dev.lebenkov.finly.user.enumiration.Role;
import dev.lebenkov.finly.user.exception.UserAlreadyExistsException;
import dev.lebenkov.finly.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserCommandServiceImpl implements UserCommandService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User register(RegisterRequest request) {
        validateRegistration(request);

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .role(Role.USER)
                .passwordHash(passwordEncoder.encode(request.password()))
                .createdAt(Instant.now())
                .build();

        return userRepository.save(user);
    }

    private void validateRegistration(RegisterRequest request) {
        if (userRepository.existsByUsernameIgnoreCase(request.username())) {
            throw new UserAlreadyExistsException("Username is already taken");
        }

        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new UserAlreadyExistsException("Email is already taken");
        }

        if (request.password().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException(
                    "Password must not exceed 72 bytes when encoded as UTF-8"
            );
        }
    }
}
