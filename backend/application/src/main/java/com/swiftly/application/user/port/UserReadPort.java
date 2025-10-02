package com.swiftly.application.user.port;
import com.swiftly.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface UserReadPort {
    Page<User> findAll(Pageable pageable);
    Optional<User> findById(Integer id);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}