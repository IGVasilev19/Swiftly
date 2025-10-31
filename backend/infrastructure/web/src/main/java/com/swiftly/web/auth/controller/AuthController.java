package com.swiftly.web.auth.controller;

import com.swiftly.application.auth.LogInService;
import com.swiftly.application.auth.port.inbound.RegisterUseCase;
import com.swiftly.domain.User;
import com.swiftly.web.auth.dto.LogInRequest;
import com.swiftly.web.auth.dto.LogInResponse;
import com.swiftly.web.auth.dto.RegisterRequest;
import com.swiftly.web.auth.mapper.LogInMapper;
import com.swiftly.web.auth.mapper.RegisterMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name="Auth")
@RequiredArgsConstructor
public class AuthController {
    private final RegisterUseCase registerService;
    private final LogInService logInService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest)
    {
        registerService.register(RegisterMapper.toCommand(registerRequest));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "success", true,
                        "message", "Account created successfully!"
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<LogInResponse> login(@RequestBody LogInRequest logInRequest)
    {
        User user = logInService.login(LogInMapper.toUser(logInRequest));

        LogInResponse response = LogInMapper.toLogInResponse(user);

        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LogInResponse> refresh(@RequestBody Map<String, String> body) {
        String token = body.get("refreshToken");
        return ResponseEntity.ok(LogInMapper.toLogInResponse(logInService.refreshToken(token)));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        logInService.logout(email);
        return ResponseEntity.noContent().build();
    }
}
