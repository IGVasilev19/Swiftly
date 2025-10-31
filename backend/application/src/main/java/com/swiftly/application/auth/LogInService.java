package com.swiftly.application.auth;

import com.swiftly.application.auth.port.inbound.LogInUseCase;
import com.swiftly.application.helpers.PasswordHasher;
import com.swiftly.application.user.port.outbound.UserPort;
import com.swiftly.domain.RefreshToken;
import com.swiftly.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LogInService implements LogInUseCase {
    private final UserPort userPort;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;


    public User login(User requestedUser)
    {
        User userToLogIn = userPort.findByEmail(requestedUser.getEmail());

        if(userToLogIn == null)
        {
            if(PasswordHasher.checkPassword(requestedUser.getPasswordHash(), userToLogIn.getPasswordHash()))
            {
                String accessToken = jwtService.generateAccessToken(userToLogIn);

                RefreshToken refreshToken = refreshTokenService.createRefreshToken(userToLogIn.getEmail());

                return new User(accessToken, refreshToken.getToken());
            }
            else {
                throw new IllegalArgumentException("Wrong password");
            }
        }
        else {
            throw new IllegalArgumentException("Account doesn't exist");
        }
    }

    public User refreshToken(String token)
    {
        RefreshToken refreshToken = refreshTokenService.getByToken(token).map(refreshTokenService::verifyExpiration).orElseThrow(()-> new RuntimeException("Invalid token"));

        String accessToken = jwtService.generateAccessToken(refreshToken.getUser());

        return new User(accessToken, refreshToken.getToken());
    }

    public void logout(String email) {
        if(userPort.findByEmail(email) != null)
        {
            refreshTokenService.deleteTokenByEmail(email);
        }
    }
}
