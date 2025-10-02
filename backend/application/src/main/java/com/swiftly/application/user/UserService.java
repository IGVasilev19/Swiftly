package com.swiftly.application.user;

import com.swiftly.application.user.port.UserReadPort;
import com.swiftly.application.user.port.UserWritePort;
import com.swiftly.domain.User;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.UnaryOperator;

@Service
@Transactional
public class UserService {
    private final UserReadPort read;
    private final UserWritePort write;

    public UserService(UserReadPort read, UserWritePort write) {
        this.read = read; this.write = write;
    }

    public Page<User> list(Pageable pageable) { return read.findAll(pageable); }
    public User get(Integer id) { return read.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found")); }
    public User create(User newUser) {
        if (read.existsByEmail(newUser.getEmail())) throw new DuplicateKeyException("Email already in use");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        newUser.setPasswordHash(passwordEncoder.encode(newUser.getPasswordHash()));
        return write.save(newUser);
    }
    public User update(Integer id, UnaryOperator<User> mutator) {
        User updatedUser = get(id);
        updatedUser = mutator.apply(updatedUser);
        return write.save(updatedUser);
    }
    public void delete(Integer id) { write.deleteById(id); }
}