// src/main/java/com/petrov/maintenance/model/MaintenanceSchedule.java
package com.petrov.maintenance.model;

import javax.persistence.*;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "maintenance_schedule")
public class MaintenanceSchedule {
    @EmbeddedId
    private ScheduleId id;

    @ManyToOne
    @MapsId("machineId")
    @JoinColumn(name = "machine_id")
    private Machine machine;

    @ManyToOne
    @MapsId("typeId")
    @JoinColumn(name = "type_id")
    private MaintenanceType maintenanceType;

    @Column(name = "next_due", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate nextDue;

    @Column(name = "last_done")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastDone;



    // Getters and setters
    public ScheduleId getId() { return id; }
    public void setId(ScheduleId id) { this.id = id; }

    public Machine getMachine() { return machine; }
    public void setMachine(Machine machine) { this.machine = machine; }

    public MaintenanceType getMaintenanceType() { return maintenanceType; }
    public void setMaintenanceType(MaintenanceType maintenanceType) { this.maintenanceType = maintenanceType; }

    public LocalDate getNextDue() { return nextDue; }
    public void setNextDue(LocalDate nextDue) { this.nextDue = nextDue; }

    public LocalDate getLastDone() { return lastDone; }
    public void setLastDone(LocalDate lastDone) { this.lastDone = lastDone; }
}