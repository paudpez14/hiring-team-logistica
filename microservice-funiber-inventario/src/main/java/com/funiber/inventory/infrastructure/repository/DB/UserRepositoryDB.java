package com.funiber.inventory.infrastructure.repository.DB;

import com.funiber.inventory.infrastructure.entity.inventory.UserEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryDB extends JpaRepository<UserEntity, Long> {
    @Query(value = "SELECT CASE " +
            "WHEN EXISTS(" +
            "SELECT 1 FROM users " +
            "WHERE users.email = :#{#userEntity.email} " +
            "and is_active = 'Y') " +
            "THEN 'TRUE' " +
            "ELSE 'FALSE' END " +
            "FROM DUAL", nativeQuery = true)
    boolean checkAccountExists(UserEntity userEntity);

    @Query(value = "SELECT * FROM users " +
            "WHERE users.email = :#{#email} " +
            "and is_active = 'Y' ", nativeQuery = true)
    Optional<UserEntity> findUserByEmail(String email);

}
