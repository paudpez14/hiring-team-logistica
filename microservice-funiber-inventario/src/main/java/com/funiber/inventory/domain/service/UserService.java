package com.funiber.inventory.domain.service;

import com.funiber.inventory.domain.dto.User;

public interface UserService {
    Boolean save(User user);
    User findUserByEmail(String email);
    boolean validatePassword(User user, String password);
}
