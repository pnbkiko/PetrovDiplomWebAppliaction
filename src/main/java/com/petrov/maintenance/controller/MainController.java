// src/main/java/com/petrov/maintenance/controller/MainController.java
package com.petrov.maintenance.controller;

import com.petrov.maintenance.service.CalendarService;
import com.petrov.maintenance.service.MachineService;
import com.petrov.maintenance.service.MaintenanceScheduleService;
import com.petrov.maintenance.service.MaintenanceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class MainController {

    @Autowired
    private MachineService machineService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private MaintenanceTypeService maintenanceTypeService;

    @Autowired
    private MaintenanceScheduleService scheduleService;

    @GetMapping("/")
    public String index(Model model) {
        long machineCount = machineService.getAllMachines().size();
        long overdueCount = calendarService.getOverdueList().size();
        long typesCount = maintenanceTypeService.getAllMaintenanceTypes().size();

        LocalDate now = LocalDate.now();
        long scheduledCount = scheduleService.getSchedulesByDateRange(
                now.withDayOfMonth(1),
                now.withDayOfMonth(now.lengthOfMonth())
        ).size();

        model.addAttribute("machineCount", machineCount);
        model.addAttribute("overdueCount", overdueCount);
        model.addAttribute("typesCount", typesCount);
        model.addAttribute("scheduledCount", scheduledCount);

        return "index";
    }

    // ДОБАВЬТЕ ЭТОТ МЕТОД:
    @GetMapping("/help")
    public String help() {
        return "help";
    }
}