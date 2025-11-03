package com.swiftly.persistence.user;

import com.swiftly.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<UserEntity, Integer> {
    boolean existsByEmail(String email);
    UserEntity findByEmail(String email);
}