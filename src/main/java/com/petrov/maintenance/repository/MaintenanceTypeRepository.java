// src/main/java/com/petrov/maintenance/repository/MaintenanceTypeRepository.java
package com.petrov.maintenance.repository;

import com.petrov.maintenance.model.MaintenanceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceTypeRepository extends JpaRepository<MaintenanceType, Long> {
}