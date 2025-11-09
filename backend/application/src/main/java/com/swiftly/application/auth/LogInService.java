package com.swiftly.application.auth;

import com.swiftly.application.auth.port.inbound.JwtUseCase;
import com.swiftly.application.auth.port.inbound.LogInUseCase;
import com.swiftly.application.auth.port.inbound.RefreshTokenUseCase;
import com.swiftly.application.helpers.PasswordHasher;
import com.swiftly.application.user.port.inbound.UserUseCase;
import com.swiftly.domain.RefreshToken;
import com.swiftly.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LogInService implements LogInUseCase {
    private final UserUseCase userService;
    private final JwtUseCase jwtService;
    private final RefreshTokenUseCase refreshTokenService;


    public User login(User requestedUser)
    {
        User userToLogIn = userService.getByEmail(requestedUser.getEmail());

        if(PasswordHasher.checkPassword(requestedUser.getPasswordHash(), userToLogIn.getPasswordHash()))
        {
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userToLogIn.getEmail());

            String accessToken = jwtService.generateAccessToken(userToLogIn);

            User authUser = new User();

            authUser.setAccessToken(accessToken);
            authUser.setRefreshToken(refreshToken.getToken());

            return authUser;
        }
        else {
            throw new IllegalArgumentException("Wrong password");
        }
    }

    public User refreshToken(String token)
    {
        RefreshToken refreshToken = refreshTokenService.getByToken(token);

        if(refreshToken == null)
        {
            return null;
        }

        refreshTokenService.verifyExpiration(refreshToken);

        String accessToken = jwtService.generateAccessToken(refreshToken.getUser());

        User authUser = new User();

        authUser.setAccessToken(accessToken);
        authUser.setRefreshToken(refreshToken.getToken());

        return authUser;
    }

    @Transactional
    public void logout(Integer userId) {
        if(userId != null)
        {
            refreshTokenService.deleteTokenById(userId);
        }
    }
}
