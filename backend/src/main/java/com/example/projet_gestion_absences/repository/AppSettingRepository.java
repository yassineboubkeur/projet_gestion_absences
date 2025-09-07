// src/main/java/com/example/projet_gestion_absences/repository/AppSettingRepository.java
package com.example.projet_gestion_absences.repository;

import com.example.projet_gestion_absences.model.entity.AppSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppSettingRepository extends JpaRepository<AppSetting, Long> {
    Optional<AppSetting> findByKey(String key);
    boolean existsByKey(String key);
}
