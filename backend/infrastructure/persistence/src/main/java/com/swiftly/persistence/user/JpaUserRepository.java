package com.swiftly.persistence.user;

import com.swiftly.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity, Integer> {
    boolean existsByEmail(String email);
    @EntityGraph(attributePaths = {"roles"})
    Optional<UserEntity> findByEmail(String email);
}