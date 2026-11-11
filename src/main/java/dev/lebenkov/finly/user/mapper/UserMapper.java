package dev.lebenkov.finly.user.mapper;

import dev.lebenkov.finly.user.entity.User;
import dev.lebenkov.finly.security.auth.dto.RegisterResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public RegisterResponse toRegisterResponse(User user) {
        return new RegisterResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
