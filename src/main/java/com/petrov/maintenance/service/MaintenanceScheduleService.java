// src/main/java/com/petrov/maintenance/service/MaintenanceScheduleService.java
package com.petrov.maintenance.service;

import com.petrov.maintenance.model.MaintenanceSchedule;
import com.petrov.maintenance.model.ScheduleId;
import com.petrov.maintenance.repository.MaintenanceScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MaintenanceScheduleService {

    @Autowired
    private MaintenanceScheduleRepository scheduleRepository;

    public List<MaintenanceSchedule> getAllSchedules() {
        return scheduleRepository.findAllWithDetails(); // Используем метод с JOIN FETCH
    }

    public MaintenanceSchedule saveSchedule(MaintenanceSchedule schedule) {
        // Убедимся, что составной ключ установлен
        if (schedule.getId() == null) {
            ScheduleId id = new ScheduleId(
                    schedule.getMachine().getId(),
                    schedule.getMaintenanceType().getId()
            );
            schedule.setId(id);
        }
        return scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Long machineId, Long typeId) {
        ScheduleId id = new ScheduleId(machineId, typeId);
        scheduleRepository.deleteById(id);
    }

    public List<MaintenanceSchedule> getOverdueSchedules() {
        return scheduleRepository.findOverdue(LocalDate.now());
    }

    public List<MaintenanceSchedule> getSchedulesByDateRange(LocalDate start, LocalDate end) {
        return scheduleRepository.findByDateRange(start, end);
    }
}