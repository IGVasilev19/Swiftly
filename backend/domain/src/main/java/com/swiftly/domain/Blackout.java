package com.swiftly.domain;

import lombok.*;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
public class Blackout {

    private Integer id;

    private Instant startAt;

    private Instant endAt;

    private Instant creationDate;

}