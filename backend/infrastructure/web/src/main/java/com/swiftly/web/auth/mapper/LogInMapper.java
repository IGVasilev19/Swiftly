package com.swiftly.web.auth.mapper;

import com.swiftly.domain.User;
import com.swiftly.web.auth.dto.LogInRequest;
import com.swiftly.web.auth.dto.LogInResponse;

public class LogInMapper {
    public static User toUser(LogInRequest logInRequest)
    {
        return new User(logInRequest.email(), logInRequest.password());
    }

    public static LogInResponse toLogInResponse(User user)
    {
        return new LogInResponse(user.getAccessToken(), user.getRefreshToken());
    }
}
