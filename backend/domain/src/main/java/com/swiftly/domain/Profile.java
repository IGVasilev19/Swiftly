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

    private String address;

    private String City;

    private String Country;

    private String postalCode;

    private String avatarUrl;
    private String locale;
    private String timezone;

    public Profile( String fullName, String phone, String address, String City, String Country, String postalCode)
    {
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.City = City;
        this.Country = Country;
        this.postalCode = postalCode;
    }

    public Profile(Integer id)
    {
        this.id = id;
        this.fullName = null;
        this.phone = null;
        this.address = null;
        this.City = null;
        this.Country = null;
        this.postalCode = null;
    }
}
