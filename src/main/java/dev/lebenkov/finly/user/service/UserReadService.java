package dev.lebenkov.finly.user.service;

import dev.lebenkov.finly.user.entity.User;

public interface UserReadService {
    User findByUsername(String username);


}
