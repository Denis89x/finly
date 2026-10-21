package dev.lebenkov.finly.security.service;

import dev.lebenkov.finly.user.entity.User;
import dev.lebenkov.finly.user.repository.UserRepository;
import dev.lebenkov.finly.user.service.UserReadService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    UserRepository userRepository;
    UserReadService userReadService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userReadService.findByUsername(username);

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .roles(user.getRole().name())
                .build();
    }
}
