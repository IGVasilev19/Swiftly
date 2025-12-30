package com.swiftly.web.auth.mapper;

import com.swiftly.application.auth.dto.RegisterCommand;
import com.swiftly.domain.Profile;
import com.swiftly.domain.User;
import com.swiftly.domain.enums.user.Role;
import com.swiftly.web.auth.dto.RegisterRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterMapperTest {

    @Test
    void toCommand_WithValidRequest_ShouldMapCorrectly() {
        String email = "test@example.com";
        String password = "password123";
        String name = "John Doe";
        String phoneNumber = "+1234567890";
        List<Role> roles = List.of(Role.OWNER);

        RegisterRequest request = new RegisterRequest(email, password, name, phoneNumber, roles);

        RegisterCommand command = RegisterMapper.toCommand(request);

        assertThat(command).isNotNull();
        assertThat(command.user()).isNotNull();
        assertThat(command.user().getEmail()).isEqualTo(email);
        assertThat(command.user().getPassword()).isEqualTo(password);
        assertThat(command.user().getRoles()).isEqualTo(roles);
        assertThat(command.profile()).isNotNull();
        assertThat(command.profile().getFullName()).isEqualTo(name);
        assertThat(command.profile().getPhone()).isEqualTo(phoneNumber);
        assertThat(command.user().getProfile()).isEqualTo(command.profile());
    }

    @Test
    void toCommand_WithMultipleRoles_ShouldMapCorrectly() {
        String email = "test@example.com";
        String password = "password123";
        String name = "John Doe";
        String phoneNumber = "+1234567890";
        List<Role> roles = List.of(Role.OWNER, Role.RENTER);

        RegisterRequest request = new RegisterRequest(email, password, name, phoneNumber, roles);

        RegisterCommand command = RegisterMapper.toCommand(request);

        assertThat(command).isNotNull();
        assertThat(command.user().getRoles()).isEqualTo(roles);
        assertThat(command.user().getRoles().size()).isEqualTo(2);
    }
}

