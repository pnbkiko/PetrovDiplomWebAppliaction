// src/main/java/com/petrov/maintenance/controller/MachineController.java
package com.petrov.maintenance.controller;

import com.petrov.maintenance.model.Machine;
import com.petrov.maintenance.service.MachineService;
import com.petrov.maintenance.service.MachineTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/machines")
public class MachineController {

    @Autowired
    private MachineService machineService;

    @Autowired
    private MachineTypeService machineTypeService;

    @GetMapping
    public String getAllMachines(Model model) {
        model.addAttribute("machines", machineService.getAllMachines());
        model.addAttribute("machineTypes", machineTypeService.getAllMachineTypes());
        model.addAttribute("machine", new Machine());
        return "machines";
    }

    @PostMapping("/add")
    public String addMachine(@ModelAttribute Machine machine) {
        // Устанавливаем тип оборудования
        if (machine.getTypeId() != null) {
            machine.setMachineType(machineTypeService.getMachineTypeById(machine.getTypeId()));
        }
        machineService.saveMachine(machine);
        return "redirect:/machines";
    }

    @PostMapping("/delete/{id}")
    public String deleteMachine(@PathVariable Long id) {
        machineService.deleteMachine(id);
        return "redirect:/machines";
    }
}