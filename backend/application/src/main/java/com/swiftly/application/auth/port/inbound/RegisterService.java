package com.swiftly.application.auth.port.inbound;

import com.swiftly.application.auth.dto.RegisterCommand;
import com.swiftly.domain.User;

public interface RegisterService {
    User register(RegisterCommand registerCommand);
}