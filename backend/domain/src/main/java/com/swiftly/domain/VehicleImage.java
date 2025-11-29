package com.swiftly.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VehicleImage {
    private Integer id;
    private Vehicle vehicle;
    private byte[] data;
    private String mimeType;
    private String fileName;
    private LocalDateTime uploadedAt =  LocalDateTime.now();
}
