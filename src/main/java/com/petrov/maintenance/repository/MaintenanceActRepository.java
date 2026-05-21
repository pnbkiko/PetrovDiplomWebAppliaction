// src/main/java/com/petrov/maintenance/repository/MaintenanceActRepository.java
package com.petrov.maintenance.repository;

import com.petrov.maintenance.model.MaintenanceAct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceActRepository extends JpaRepository<MaintenanceAct, Long> {
}