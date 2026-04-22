// src/main/java/com/petrov/maintenance/controller/CalendarController.java
package com.petrov.maintenance.controller;

import com.petrov.maintenance.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    @GetMapping
    public String getCalendar(@RequestParam(required = false) Integer year,
                              @RequestParam(required = false) Integer month,
                              Model model) {

        LocalDate today = LocalDate.now();
        if (year == null) year = today.getYear();
        if (month == null) month = today.getMonthValue();

        YearMonth yearMonth = YearMonth.of(year, month);

        // Создаем календарь на 6 недель
        List<List<LocalDate>> calendarWeeks = createCalendarWeeks(yearMonth);

        // Получаем данные для календаря
        Map<LocalDate, List<String>> calendarData = calendarService.getCalendarData(year, month);

        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("monthName", yearMonth.getMonth().getDisplayName(TextStyle.FULL, new Locale("ru")));
        model.addAttribute("calendarWeeks", calendarWeeks);
        model.addAttribute("calendarData", calendarData);
        model.addAttribute("today", today);
        model.addAttribute("overdue", calendarService.getOverdueList());

        // Навигация
        YearMonth prevMonth = yearMonth.minusMonths(1);
        YearMonth nextMonth = yearMonth.plusMonths(1);

        model.addAttribute("prevMonth", prevMonth.getYear());
        model.addAttribute("prevMonthNum", prevMonth.getMonthValue());
        model.addAttribute("nextMonth", nextMonth.getYear());
        model.addAttribute("nextMonthNum", nextMonth.getMonthValue());

        return "calendar";
    }

    private List<List<LocalDate>> createCalendarWeeks(YearMonth yearMonth) {
        List<List<LocalDate>> weeks = new ArrayList<>();

        // Первый день месяца
        LocalDate firstDay = yearMonth.atDay(1);
        // Первый день отображаемого календаря (понедельник недели)
        LocalDate calendarStart = firstDay.with(DayOfWeek.MONDAY);

        LocalDate currentDay = calendarStart;

        // 6 недель календаря
        for (int week = 0; week < 6; week++) {
            List<LocalDate> weekDays = new ArrayList<>();
            for (int day = 0; day < 7; day++) {
                weekDays.add(currentDay);
                currentDay = currentDay.plusDays(1);
            }
            weeks.add(weekDays);
        }

        return weeks;
    }
}