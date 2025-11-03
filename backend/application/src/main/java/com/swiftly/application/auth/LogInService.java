package com.swiftly.application.auth;

import com.swiftly.application.auth.port.inbound.LogInUseCase;
import com.swiftly.application.helpers.PasswordHasher;
import com.swiftly.application.user.UserService;
import com.swiftly.domain.RefreshToken;
import com.swiftly.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LogInService implements LogInUseCase {
    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;


    public User login(User requestedUser)
    {
        User userToLogIn = userService.getByEmail(requestedUser.getEmail());

        if(PasswordHasher.checkPassword(requestedUser.getPasswordHash(), userToLogIn.getPasswordHash()))
        {
            refreshTokenService.createRefreshToken(userToLogIn.getEmail());

            String accessToken = jwtService.generateAccessToken(userToLogIn);

            return new User(accessToken);
        }
        else {
            throw new IllegalArgumentException("Wrong password");
        }
    }

    public User refreshToken(String token)
    {
        RefreshToken refreshToken = refreshTokenService.getByToken(token).map(refreshTokenService::verifyExpiration).orElseThrow(()-> new RuntimeException("Invalid token"));

        String accessToken = jwtService.generateAccessToken(refreshToken.getUser());

        return new User(accessToken, refreshToken.getToken());
    }

    public void logout(String email) {
        if(userService.getByEmail(email) != null)
        {
            refreshTokenService.deleteTokenByEmail(email);
        }
    }
}
