package com.petrov.maintenance.dto;

import java.time.LocalDate;

public class ScheduleDTO {
    private Long machineId;
    private String machineModel;
    private String invNumber;
    private Long typeId;
    private String typeName;
    private LocalDate nextDue;
    private LocalDate lastDone;

    // Конструкторы
    public ScheduleDTO() {}

    public ScheduleDTO(Long machineId, String machineModel, String invNumber,
                       Long typeId, String typeName, LocalDate nextDue, LocalDate lastDone) {
        this.machineId = machineId;
        this.machineModel = machineModel;
        this.invNumber = invNumber;
        this.typeId = typeId;
        this.typeName = typeName;
        this.nextDue = nextDue;
        this.lastDone = lastDone;
    }

    // Геттеры и сеттеры
    public Long getMachineId() { return machineId; }
    public void setMachineId(Long machineId) { this.machineId = machineId; }

    public String getMachineModel() { return machineModel; }
    public void setMachineModel(String machineModel) { this.machineModel = machineModel; }

    public String getInvNumber() { return invNumber; }
    public void setInvNumber(String invNumber) { this.invNumber = invNumber; }

    public Long getTypeId() { return typeId; }
    public void setTypeId(Long typeId) { this.typeId = typeId; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    public LocalDate getNextDue() { return nextDue; }
    public void setNextDue(LocalDate nextDue) { this.nextDue = nextDue; }

    public LocalDate getLastDone() { return lastDone; }
    public void setLastDone(LocalDate lastDone) { this.lastDone = lastDone; }
}