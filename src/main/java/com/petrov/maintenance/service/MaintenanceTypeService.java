// src/main/java/com/petrov/maintenance/service/MaintenanceTypeService.java
package com.petrov.maintenance.service;

import com.petrov.maintenance.model.MaintenanceType;
import com.petrov.maintenance.repository.MaintenanceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintenanceTypeService {

    @Autowired
    private MaintenanceTypeRepository maintenanceTypeRepository;

    public List<MaintenanceType> getAllMaintenanceTypes() {
        return maintenanceTypeRepository.findAll();
    }

    public MaintenanceType saveMaintenanceType(MaintenanceType maintenanceType) {
        return maintenanceTypeRepository.save(maintenanceType);
    }

    public MaintenanceType getMaintenanceTypeById(Long id) {
        return maintenanceTypeRepository.findById(id).orElse(null);
    }

    public void deleteMaintenanceType(Long id) {
        maintenanceTypeRepository.deleteById(id);
    }
    public MaintenanceType getMaintenanceTypeByID(Long id) {
        return maintenanceTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MaintenanceType not found with id: " + id));
    }

}