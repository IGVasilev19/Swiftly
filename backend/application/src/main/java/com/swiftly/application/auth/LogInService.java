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

    public void logout(String email) {
        if(userService.getByEmail(email) != null)
        {
            refreshTokenService.deleteTokenByEmail(email);
        }
    }
}
