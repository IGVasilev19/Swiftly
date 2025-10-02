package com.swiftly.persistence.user;

import com.swiftly.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

interface JpaUserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}