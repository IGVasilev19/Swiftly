package com.swiftly.web.user.controller;

import com.swiftly.application.user.UserService;
import com.swiftly.application.user.port.inbound.UserUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/users")
@PreAuthorize("isAuthenticated()")
@Tag(name = "Users")
public class UserController {
    private final UserUseCase service;

    public UserController(UserService service)
    {
        this.service = service;
    }
}