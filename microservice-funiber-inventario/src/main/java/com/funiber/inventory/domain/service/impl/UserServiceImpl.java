package com.funiber.inventory.domain.service.impl;

import java.util.Optional;

import com.funiber.inventory.domain.dto.User;
import com.funiber.inventory.domain.exception.CategoryExistException;
import com.funiber.inventory.domain.exception.UserNotFoundException;
import com.funiber.inventory.domain.repository.UserRepository;
import com.funiber.inventory.domain.service.UserService;

public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Boolean save(User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede ser nula o vacía");
        }
        if (userRepository.checkAccountExists(user)) {
            throw new CategoryExistException(user.getEmail());
        }
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error al cifrar la contraseña", e);
        }
    }

    @Override
    public User findUserByEmail(String email) {

        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(email);
        } else {
            return optionalUser.get();
        }

    }

    @Override
    public boolean validatePassword(User user, String password) {
        return userRepository.validatePassword(user, password);
    }

}
