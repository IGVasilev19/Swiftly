package com.swiftly.web.user.controller;

import com.swiftly.web.user.dto.UserCreateRequest;
import com.swiftly.web.user.dto.UserResponse;
import com.swiftly.web.user.dto.UserUpdateRequest;
import com.swiftly.web.user.mapper.UserMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users")
public class UserController {
    private final com.swiftly.application.user.UserService service;

    public UserController(com.swiftly.application.user.UserService service)
    {
        this.service = service;
    }

    @GetMapping
    public Page<UserResponse> list(Pageable pageable) {
        return service.list(pageable).map(UserMapper::toResponse);
    }

    @GetMapping("/{id}") public UserResponse get(@PathVariable Integer id) {
        return UserMapper.toResponse(service.get(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody UserCreateRequest req) {
        var saved = service.create(UserMapper.toNewEntity(req));
        return UserMapper.toResponse(saved);
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Integer id, @Valid @RequestBody UserUpdateRequest req) {
        var updated = service.update(id, u -> {
            if (req.fullName()!=null) u.setFullName(req.fullName());
            if (req.email()!=null)   u.setEmail(req.email());
            if (req.password()!=null && !req.password().isBlank()) u.setPasswordHash(req.password());
            if (req.role()!=null)    u.setRole(req.role());
            if (req.status()!=null)  u.setStatus(req.status());
            return u;
        });
        return UserMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) { service.delete(id); }
}