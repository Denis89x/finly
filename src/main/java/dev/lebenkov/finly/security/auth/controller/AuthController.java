package dev.lebenkov.finly.security.auth.controller;

import dev.lebenkov.finly.security.auth.dto.RegisterRequest;
import dev.lebenkov.finly.user.mapper.UserMapper;
import dev.lebenkov.finly.security.auth.dto.RegisterResponse;
import dev.lebenkov.finly.user.service.UserCommandService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    UserCommandService userCommandService;
    UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userMapper.toRegisterResponse(
                        userCommandService.register(request)
                ));
    }
}
