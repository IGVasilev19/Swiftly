package com.swiftly.web.user.mapper;

import com.swiftly.domain.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class UserMapper {
//    public static UserResponse toResponse(User existingUser) {
//        return new UserResponse(existingUser.getId(), existingUser.getEmail(), existingUser.getRole(), existingUser.getStatus());
//    }
//    public static User toNewEntity(UserCreateRequest newUser) {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        return User.builder().email(newUser.email())
//                .passwordHash(passwordEncoder.encode(newUser.password()))
//                .role(newUser.role()).status(Boolean.TRUE.equals(newUser.status()))
//                .build();
//    }
}