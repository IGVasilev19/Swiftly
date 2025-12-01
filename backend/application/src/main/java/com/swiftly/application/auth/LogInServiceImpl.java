package com.swiftly.application.auth;

import com.swiftly.application.auth.port.inbound.JwtService;
import com.swiftly.application.auth.port.inbound.LogInService;
import com.swiftly.application.auth.port.inbound.RefreshTokenService;
import com.swiftly.application.helpers.PasswordHasher;
import com.swiftly.application.user.port.inbound.UserService;
import com.swiftly.domain.RefreshToken;
import com.swiftly.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LogInServiceImpl implements LogInService {
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

        refreshToken = refreshTokenService.verifyExpiration(refreshToken);
        
        if (refreshToken == null) {
            return null;
        }

        User fullUser = userService.getByEmail(refreshToken.getUser().getEmail());

        String accessToken = jwtService.generateAccessToken(fullUser);

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
