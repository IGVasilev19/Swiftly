package com.swiftly.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "vehicle_images")
public class VehicleImageEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "vehicle_id", nullable = false)
        private VehicleEntity vehicle;

        @Lob
        @Column(name = "data", nullable = false)
        private byte[] data;

        @Column(name = "mime_type", nullable = false)
        private String mimeType;

        @Column(name = "file_name")
        private String fileName;

        @Column(name = "uploaded_at", nullable = false)
        private LocalDateTime uploadedAt = LocalDateTime.now();


        public VehicleImageEntity(Integer id, VehicleEntity vehicle, byte[] data, String mimeType, String fileName, LocalDateTime uploadedAt) {
            this.id = id;
            this.vehicle = vehicle;
            this.data = data;
            this.mimeType = mimeType;
            this.fileName = fileName;
            this.uploadedAt = uploadedAt;
        }

        public VehicleImageEntity (byte[] data, String mimeType, String fileName,  LocalDateTime uploadedAt)
        {
            this.data = data;
            this.mimeType = mimeType;
            this.fileName = fileName;
            this.uploadedAt = uploadedAt;
        }

        public VehicleImageEntity(Integer id) {
            this.id = id;
        }
}
