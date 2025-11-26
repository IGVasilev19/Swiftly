package com.swiftly.domain;
import com.swiftly.domain.enums.user.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class User implements UserDetails {
    private Integer id;

    private String email;

    private String passwordHash;

    private List<Role> roles;

    private Profile profile;

    private String accessToken;

    private String refreshToken;

    public void attachProfile(Profile p) {
        this.profile = p;
        if (p != null) p.setUser(this);
    }

    public User(Integer id, String email, String passwordHash, List<Role> roles)
    {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.roles = roles;
    }

    public User(String email, String passwordHash, List<Role> roles)
    {
        this.email = email;
        this.passwordHash = passwordHash;
        this.roles = roles;
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
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList();
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
