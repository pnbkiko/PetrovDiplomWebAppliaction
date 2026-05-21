// src/main/java/com/petrov/maintenance/service/MaintenanceActService.java
package com.petrov.maintenance.service;

import com.petrov.maintenance.model.MaintenanceAct;
import com.petrov.maintenance.repository.MaintenanceActRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintenanceActService {

    @Autowired
    private MaintenanceActRepository maintenanceActRepository;

    public List<MaintenanceAct> getAllMaintenanceActs() {
        return maintenanceActRepository.findAll();
    }

    public MaintenanceAct saveMaintenanceAct(MaintenanceAct maintenanceAct) {
        return maintenanceActRepository.save(maintenanceAct);
    }

    public MaintenanceAct getMaintenanceActById(Long id) {
        return maintenanceActRepository.findById(id).orElse(null);
    }

    public void deleteMaintenanceAct(Long id) {
        maintenanceActRepository.deleteById(id);
    }
}