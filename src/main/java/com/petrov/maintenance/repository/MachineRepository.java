// src/main/java/com/petrov/maintenance/repository/MachineRepository.java
package com.petrov.maintenance.repository;

import com.petrov.maintenance.model.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {
}