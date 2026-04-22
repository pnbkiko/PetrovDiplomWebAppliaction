// src/main/java/com/petrov/maintenance/controller/MaintenanceActController.java
package com.petrov.maintenance.controller;

import com.petrov.maintenance.model.MaintenanceAct;
import com.petrov.maintenance.service.MachineService;
import com.petrov.maintenance.service.MaintenanceActService;
import com.petrov.maintenance.service.MaintenanceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/maintenance-acts")
public class MaintenanceActController {

    @Autowired
    private MaintenanceActService maintenanceActService;

    @Autowired
    private MachineService machineService;

    @Autowired
    private MaintenanceTypeService maintenanceTypeService;

    @GetMapping
    public String getAllMaintenanceActs(Model model) {
        model.addAttribute("acts", maintenanceActService.getAllMaintenanceActs());
        model.addAttribute("machines", machineService.getAllMachines());
        model.addAttribute("types", maintenanceTypeService.getAllMaintenanceTypes());
        model.addAttribute("act", new MaintenanceAct());
        return "maintenance-acts";
    }

    @PostMapping("/add")
    public String addMaintenanceAct(@ModelAttribute MaintenanceAct act) {
        maintenanceActService.saveMaintenanceAct(act);
        return "redirect:/maintenance-acts";
    }

    @PostMapping("/delete/{id}")
    public String deleteMaintenanceAct(@PathVariable Long id) {
        maintenanceActService.deleteMaintenanceAct(id);
        return "redirect:/maintenance-acts";
    }
}