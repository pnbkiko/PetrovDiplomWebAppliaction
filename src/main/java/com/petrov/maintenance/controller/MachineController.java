// src/main/java/com/petrov/maintenance/controller/MachineController.java
package com.petrov.maintenance.controller;

import com.petrov.maintenance.model.Machine;
import com.petrov.maintenance.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/machines")
public class MachineController {

    @Autowired
    private MachineService machineService;

    @GetMapping
    public String getAllMachines(Model model) {
        model.addAttribute("machines", machineService.getAllMachines());
        model.addAttribute("machine", new Machine());
        return "machines";
    }

    @PostMapping("/add")
    public String addMachine(@ModelAttribute Machine machine) {
        machineService.saveMachine(machine);
        return "redirect:/machines";
    }

    @PostMapping("/delete/{id}")
    public String deleteMachine(@PathVariable Long id) {
        machineService.deleteMachine(id);
        return "redirect:/machines";
    }
}