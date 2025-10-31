package com.swiftly.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    private Long id;

    private String token;

    private Instant expiryDate;

    private User user;

    private boolean revoked;
}
