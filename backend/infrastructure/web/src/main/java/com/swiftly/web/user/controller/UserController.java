package com.swiftly.web.user.controller;

import com.swiftly.application.user.UserService;
import com.swiftly.application.user.port.inbound.UserUseCase;
import com.swiftly.web.user.dto.UserCreateRequest;
import com.swiftly.web.user.dto.UserResponse;
import com.swiftly.web.user.mapper.UserMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/users")
@Tag(name = "Users")
public class UserController {
    private final UserUseCase service;

    public UserController(UserService service)
    {
        this.service = service;
    }

    @GetMapping("/{id}") public UserResponse getById(@PathVariable Integer id) {
        return UserMapper.toResponse(service.getById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody UserCreateRequest req) {
        var saved = service.create(UserMapper.toNewEntity(req));
        return UserMapper.toResponse(saved);
    }
}