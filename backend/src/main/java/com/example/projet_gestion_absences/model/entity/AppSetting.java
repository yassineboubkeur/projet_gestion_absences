package com.example.projet_gestion_absences.model.entity;

// src/main/java/com/example/projet_gestion_absences/model/entity/AppSetting.java
//package com.example.projet_gestion_absences.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "app_settings", uniqueConstraints = @UniqueConstraint(columnNames = "cfg_key"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AppSetting {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cfg_key", nullable = false, length = 100)
    private String key;

    @Column(name = "cfg_value", columnDefinition = "TEXT")
    private String value;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist @PreUpdate
    void touch() { this.updatedAt = Instant.now(); }
}
