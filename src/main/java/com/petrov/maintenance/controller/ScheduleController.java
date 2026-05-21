// src/main/java/com/petrov/maintenance/controller/ScheduleController.java
package com.petrov.maintenance.controller;

import com.petrov.maintenance.model.MaintenanceSchedule;
import com.petrov.maintenance.model.ScheduleId;
import com.petrov.maintenance.service.MachineService;
import com.petrov.maintenance.service.MaintenanceScheduleService;
import com.petrov.maintenance.service.MaintenanceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private MaintenanceScheduleService scheduleService;

    @Autowired
    private MachineService machineService;

    @Autowired
    private MaintenanceTypeService maintenanceTypeService;

    @GetMapping
    public String getAllSchedules(Model model) {
        try {
            // Получаем данные
            List<MaintenanceSchedule> schedules = scheduleService.getAllSchedules();

            // Подсчитываем статистику
            long overdueCount = 0;
            long pendingCount = 0;

            if (schedules != null && !schedules.isEmpty()) {
                overdueCount = schedules.stream()
                        .filter(s -> s.getNextDue() != null && s.getNextDue().isBefore(LocalDate.now()))
                        .count();
                pendingCount = schedules.size() - overdueCount;

                // Отладочная информация
                System.out.println("=== DEBUG SCHEDULE DATA ===");
                System.out.println("Total schedules: " + schedules.size());
                for (int i = 0; i < schedules.size(); i++) {
                    MaintenanceSchedule s = schedules.get(i);
                    System.out.println("Schedule " + i + ":");
                    System.out.println("  Machine: " + (s.getMachine() != null ?
                            s.getMachine().getId() + " - " + s.getMachine().getModel() : "NULL"));
                    System.out.println("  Type: " + (s.getMaintenanceType() != null ?
                            s.getMaintenanceType().getId() + " - " + s.getMaintenanceType().getName() : "NULL"));
                    System.out.println("  Next due: " + s.getNextDue());
                    System.out.println("  Last done: " + s.getLastDone());
                }
            }

            // Добавляем в модель
            model.addAttribute("schedules", schedules);
            model.addAttribute("machines", machineService.getAllMachines());
            model.addAttribute("types", maintenanceTypeService.getAllMaintenanceTypes());
            model.addAttribute("overdueCount", overdueCount);
            model.addAttribute("pendingCount", pendingCount);

            // Создаем пустой объект для формы
            MaintenanceSchedule schedule = new MaintenanceSchedule();
            schedule.setId(new ScheduleId());
            model.addAttribute("schedule", schedule);

        } catch (Exception e) {
            System.err.println("ERROR loading schedules: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Ошибка загрузки данных: " + e.getMessage());
            model.addAttribute("schedules", List.of());
            model.addAttribute("machines", List.of());
            model.addAttribute("types", List.of());
            model.addAttribute("overdueCount", 0);
            model.addAttribute("pendingCount", 0);
        }

        return "schedule";
    }

    @PostMapping("/add")
    public String addSchedule(@ModelAttribute("schedule") MaintenanceSchedule schedule,
                              RedirectAttributes redirectAttributes) {
        try {
            // Проверяем, что составной ключ установлен через сеттеры объекта
            // В форме должны быть поля: id.machineId и id.typeId
            if (schedule.getId() == null) {
                ScheduleId id = new ScheduleId();
                schedule.setId(id);
            }

            // Получаем связанные объекты по ID из составного ключа
            if (schedule.getId().getMachineId() != null) {
                schedule.setMachine(machineService.getMachineById(schedule.getId().getMachineId()));
            }
            if (schedule.getId().getTypeId() != null) {
                schedule.setMaintenanceType(maintenanceTypeService.getMaintenanceTypeByID(schedule.getId().getTypeId()));
            }

            scheduleService.saveSchedule(schedule);
            redirectAttributes.addFlashAttribute("success", "Запись успешно добавлена в график ТО!");

        } catch (Exception e) {
            System.err.println("ERROR adding schedule: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Ошибка добавления: " + e.getMessage());
        }

        return "redirect:/schedule";
    }

    @PostMapping("/delete")
    public String deleteSchedule(@RequestParam Long machineId,
                                 @RequestParam Long typeId,
                                 RedirectAttributes redirectAttributes) {
        try {
            scheduleService.deleteSchedule(machineId, typeId);
            redirectAttributes.addFlashAttribute("success", "Запись успешно удалена!");
        } catch (Exception e) {
            System.err.println("ERROR deleting schedule: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Ошибка удаления: " + e.getMessage());
        }

        return "redirect:/schedule";
    }
}