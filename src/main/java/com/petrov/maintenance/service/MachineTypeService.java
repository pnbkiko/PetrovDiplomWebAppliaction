// src/main/java/com/petrov/maintenance/service/MachineTypeService.java
package com.petrov.maintenance.service;

import com.petrov.maintenance.model.MachineType;
import com.petrov.maintenance.repository.MachineTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MachineTypeService {

    @Autowired
    private MachineTypeRepository machineTypeRepository;

    public List<MachineType> getAllMachineTypes() {
        return machineTypeRepository.findAll();
    }

    public MachineType getMachineTypeById(Long id) {
        return machineTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MachineType not found with id: " + id));
    }

    public MachineType saveMachineType(MachineType machineType) {
        return machineTypeRepository.save(machineType);
    }

    public void deleteMachineType(Long id) {
        machineTypeRepository.deleteById(id);
    }
}