package com.funiber.inventory.infrastructure.repository;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.funiber.inventory.domain.dto.User;
import com.funiber.inventory.domain.repository.UserRepository;
import com.funiber.inventory.infrastructure.entity.inventory.UserEntity;
import com.funiber.inventory.infrastructure.repository.DB.UserRepositoryDB;

@Repository
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    UserRepositoryDB userRepositoryDB;

    @Override
    public void save(User user) {
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        userRepositoryDB.save(userEntity);
    }

    @Override
    public boolean checkAccountExists(User user) {
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        return userRepositoryDB.checkAccountExists(userEntity);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        Optional<UserEntity> optionalUser = userRepositoryDB.findUserByEmail(email);
        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }
        User data = modelMapper.map(optionalUser.get(), User.class);
        return Optional.of(data);

    }

    @Override
    public boolean validatePassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public Optional<User> findUserById(String id) {
        Optional<UserEntity> usOptional = userRepositoryDB.findById(Long.parseLong(id));
        if(usOptional.isPresent()){
            return Optional.of(modelMapper.map(usOptional.get(), User.class));
        }else{
           return Optional.empty();
        }
    }

}
