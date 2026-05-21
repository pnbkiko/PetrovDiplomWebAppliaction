// src/main/java/com/petrov/maintenance/controller/MaintenanceTypeController.java
package com.petrov.maintenance.controller;

import com.petrov.maintenance.model.MaintenanceType;
import com.petrov.maintenance.service.MaintenanceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/maintenance-types")
public class MaintenanceTypeController {

    @Autowired
    private MaintenanceTypeService maintenanceTypeService;

    @GetMapping
    public String getAllMaintenanceTypes(Model model) {
        model.addAttribute("types", maintenanceTypeService.getAllMaintenanceTypes());
        model.addAttribute("type", new MaintenanceType());
        return "maintenance-types";
    }

    @PostMapping("/add")
    public String addMaintenanceType(@ModelAttribute MaintenanceType type) {
        maintenanceTypeService.saveMaintenanceType(type);
        return "redirect:/maintenance-types";
    }

    @PostMapping("/delete/{id}")
    public String deleteMaintenanceType(@PathVariable Long id) {
        maintenanceTypeService.deleteMaintenanceType(id);
        return "redirect:/maintenance-types";
    }
}