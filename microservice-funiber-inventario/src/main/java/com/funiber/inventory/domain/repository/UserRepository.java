package com.funiber.inventory.domain.repository;

import java.util.Optional;

import com.funiber.inventory.domain.dto.User;

public interface UserRepository {
    boolean checkAccountExists(User user);

    void save(User user);

    Optional<User> findUserByEmail(String email);
    Optional<User> findUserById(String id);
    boolean validatePassword(User user, String password);
}
