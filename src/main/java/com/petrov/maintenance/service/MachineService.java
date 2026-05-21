// src/main/java/com/petrov/maintenance/service/MachineService.java
package com.petrov.maintenance.service;

import com.petrov.maintenance.model.Machine;
import com.petrov.maintenance.repository.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MachineService {

    @Autowired
    private MachineRepository machineRepository;

    public List<Machine> getAllMachines() {
        return machineRepository.findAll();
    }

    public Machine saveMachine(Machine machine) {
        return machineRepository.save(machine);
    }

    public Machine getMachineById(Long id) {
        return machineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Machine not found with id: " + id));
    }

    public void deleteMachine(Long id) {
        machineRepository.deleteById(id);
    }
}