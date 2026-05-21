// src/main/java/com/petrov/maintenance/service/CalendarService.java
package com.petrov.maintenance.service;

import com.petrov.maintenance.model.MaintenanceSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Service
public class CalendarService {

    @Autowired
    private MaintenanceScheduleService scheduleService;

    public Map<LocalDate, List<String>> getCalendarData(int year, int month) {
        try {
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();

            List<MaintenanceSchedule> schedules = scheduleService.getSchedulesByDateRange(startDate, endDate);

            Map<LocalDate, List<String>> calendarData = new HashMap<>();

            for (MaintenanceSchedule schedule : schedules) {
                LocalDate dueDate = schedule.getNextDue();
                if (dueDate != null &&
                        !dueDate.isBefore(startDate) &&
                        !dueDate.isAfter(endDate)) {

                    // ПОЛНЫЙ ТЕКСТ БЕЗ ОБРЕЗАНИЯ
                    String machineModel = schedule.getMachine() != null ?
                            schedule.getMachine().getModel() : "Неизвестно";
                    String maintenanceType = schedule.getMaintenanceType() != null ?
                            schedule.getMaintenanceType().getName() : "Неизвестно";

                    String info = machineModel + " - " + maintenanceType;

                    calendarData.computeIfAbsent(dueDate, k -> new ArrayList<>()).add(info);
                }
            }

            return calendarData;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public List<Map<String, Object>> getOverdueList() {
        try {
            List<MaintenanceSchedule> overdue = scheduleService.getOverdueSchedules();
            List<Map<String, Object>> result = new ArrayList<>();

            for (MaintenanceSchedule schedule : overdue) {
                Map<String, Object> item = new HashMap<>();
                item.put("machine", schedule.getMachine() != null ? schedule.getMachine().getModel() : "Не указано");
                item.put("invNumber", schedule.getMachine() != null ? schedule.getMachine().getInvNumber() : "—");
                item.put("type", schedule.getMaintenanceType() != null ? schedule.getMaintenanceType().getName() : "Не указано");
                item.put("dueDate", schedule.getNextDue());

                // Расчет просрочки
                if (schedule.getNextDue() != null) {
                    long days = LocalDate.now().toEpochDay() - schedule.getNextDue().toEpochDay();
                    item.put("daysOverdue", days > 0 ? days : 0);
                } else {
                    item.put("daysOverdue", 0);
                }

                result.add(item);
            }

            // Сортировка по убыванию просрочки
            result.sort((a, b) -> Long.compare(
                    (Long) b.get("daysOverdue"),
                    (Long) a.get("daysOverdue")
            ));

            return result;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}