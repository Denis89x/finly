package dev.lebenkov.finly.user.service;

import dev.lebenkov.finly.user.entity.User;
import dev.lebenkov.finly.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserReadServiceImpl implements UserReadService {

    UserRepository userRepository;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User nor found: " + username
                ));
    }
}
