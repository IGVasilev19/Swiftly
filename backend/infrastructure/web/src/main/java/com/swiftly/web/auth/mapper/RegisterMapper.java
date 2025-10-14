package com.swiftly.web.auth.mapper;

import com.swiftly.application.auth.dto.RegisterCommand;
import com.swiftly.web.auth.dto.RegisterRequest;

public class RegisterMapper {
    public static RegisterCommand toCommand(RegisterRequest registerRequest)
    {
        return new RegisterCommand(registerRequest.email(), registerRequest.password(), registerRequest.fullName(), registerRequest.phoneNumber(), registerRequest.role(),registerRequest.address(), registerRequest.city(), registerRequest.country(), registerRequest.postalCode());
    }
}
