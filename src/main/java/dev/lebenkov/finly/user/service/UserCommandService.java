package dev.lebenkov.finly.user.service;

import dev.lebenkov.finly.security.auth.dto.RegisterRequest;
import dev.lebenkov.finly.user.entity.User;

public interface UserCommandService {

    User register(RegisterRequest request);
}
