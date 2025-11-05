package com.swiftly.domain;
import com.swiftly.domain.enums.user.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class User implements UserDetails {
    private Integer id;

    private String email;

    private String passwordHash;

    private Role role;

    private Profile profile;

    String accessToken;

    String refreshToken;

    public void attachProfile(Profile p) {
        this.profile = p;
        if (p != null) p.setUser(this);
    }

    public User(Integer id, String email, String passwordHash, Role role)
    {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public User(String email, String passwordHash, Role role)
    {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public User(Integer id,String email, String passwordHash)
    {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public User(String email, String passwordHash)
    {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
