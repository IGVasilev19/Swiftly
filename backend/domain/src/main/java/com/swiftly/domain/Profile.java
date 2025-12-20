package com.swiftly.domain;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Profile {

    private Integer id;

    private User user;

    private String fullName;

    private String phone;

    private String avatarUrl;


    public Profile(Integer id, String fullName, String phone, String avatarUrl)
    {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
    }

    public Profile( String fullName, String phone)
    {
        this.fullName = fullName;
        this.phone = phone;
    }

    public Profile(Integer id)
    {
        this.id = id;
        this.fullName = null;
        this.phone = null;
    }
}
