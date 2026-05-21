// src/main/java/com/petrov/maintenance/repository/MachineTypeRepository.java
package com.petrov.maintenance.repository;

import com.petrov.maintenance.model.MachineType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineTypeRepository extends JpaRepository<MachineType, Long> {
    MachineType findByName(String name);
}