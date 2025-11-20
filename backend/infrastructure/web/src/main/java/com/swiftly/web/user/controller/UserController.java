package com.swiftly.web.user.controller;

import com.swiftly.application.user.UserServiceImpl;
import com.swiftly.application.user.port.inbound.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/users")
@PreAuthorize("isAuthenticated()")
@Tag(name = "Users")
public class UserController {
    private final UserService service;

    public UserController(UserServiceImpl service)
    {
        this.service = service;
    }
}