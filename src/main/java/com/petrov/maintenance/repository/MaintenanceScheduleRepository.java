// src/main/java/com/petrov/maintenance/repository/MaintenanceScheduleRepository.java
package com.petrov.maintenance.repository;

import com.petrov.maintenance.model.MaintenanceSchedule;
import com.petrov.maintenance.model.ScheduleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaintenanceScheduleRepository extends JpaRepository<MaintenanceSchedule, ScheduleId> {

    // Переопределяем стандартный findAll для загрузки связанных сущностей
    @Override
    @Query("SELECT DISTINCT s FROM MaintenanceSchedule s " +
            "LEFT JOIN FETCH s.machine m " +
            "LEFT JOIN FETCH s.maintenanceType t " +
            "ORDER BY s.nextDue ASC")
    List<MaintenanceSchedule> findAll();

    // Добавляем метод для получения всех записей с деталями (аналогичный findAll)
    @Query("SELECT DISTINCT s FROM MaintenanceSchedule s " +
            "LEFT JOIN FETCH s.machine m " +
            "LEFT JOIN FETCH s.maintenanceType t " +
            "ORDER BY s.nextDue ASC")
    List<MaintenanceSchedule> findAllWithDetails();

    @Query("SELECT s FROM MaintenanceSchedule s WHERE s.nextDue < :today")
    List<MaintenanceSchedule> findOverdue(@Param("today") LocalDate today);

    @Query("SELECT s FROM MaintenanceSchedule s WHERE s.nextDue BETWEEN :startDate AND :endDate")
    List<MaintenanceSchedule> findByDateRange(@Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);
}