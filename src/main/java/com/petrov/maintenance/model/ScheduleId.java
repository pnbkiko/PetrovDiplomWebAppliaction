// src/main/java/com/petrov/maintenance/model/ScheduleId.java
package com.petrov.maintenance.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ScheduleId implements Serializable {
    @Column(name = "machine_id")
    private Long machineId;

    @Column(name = "type_id")
    private Long typeId;

    public ScheduleId() {}

    public ScheduleId(Long machineId, Long typeId) {
        this.machineId = machineId;
        this.typeId = typeId;
    }

    // Getters and setters
    public Long getMachineId() { return machineId; }
    public void setMachineId(Long machineId) { this.machineId = machineId; }

    public Long getTypeId() { return typeId; }
    public void setTypeId(Long typeId) { this.typeId = typeId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleId that = (ScheduleId) o;
        return Objects.equals(machineId, that.machineId) &&
                Objects.equals(typeId, that.typeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(machineId, typeId);
    }
}