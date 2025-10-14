package com.swiftly.web.auth.controller;

import com.swiftly.application.auth.port.inbound.RegisterUseCase;
import com.swiftly.web.auth.dto.RegisterRequest;
import com.swiftly.web.auth.mapper.RegisterMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1/api/auth")
@Tag(name="Auth")
public class AuthController {
    private final RegisterUseCase registerService;

    public AuthController(RegisterUseCase registerService)
    {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest)
    {
        registerService.register(RegisterMapper.toCommand(registerRequest));

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Registration successfull!"));
    }
}
