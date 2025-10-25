package com.swiftly.domain;
import com.swiftly.domain.enums.user.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter @Builder
public class User {
    private Integer id;

    private String email;

    private String passwordHash;

    private Role role;

    private Boolean status;

    private Profile profile;

    public void attachProfile(Profile p) {
        this.profile = p;
        if (p != null) p.setUser(this);
    }

    public User(String email, String passwordHash, Role role,  Boolean status)
    {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.status = status;
    }
}
