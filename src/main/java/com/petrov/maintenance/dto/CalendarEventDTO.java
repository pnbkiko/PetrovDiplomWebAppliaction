package com.petrov.maintenance.dto;

import java.time.LocalDate;

public class CalendarEventDTO {
    private LocalDate date;
    private String machineInvNumber;
    private String machineModel;
    private String typeName;
    private String status;
    private Long machineId;
    private Long typeId;

    // Конструкторы
    public CalendarEventDTO() {}

    public CalendarEventDTO(LocalDate date, String machineInvNumber, String machineModel,
                            String typeName, String status, Long machineId, Long typeId) {
        this.date = date;
        this.machineInvNumber = machineInvNumber;
        this.machineModel = machineModel;
        this.typeName = typeName;
        this.status = status;
        this.machineId = machineId;
        this.typeId = typeId;
    }

    // Геттеры и сеттеры
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getMachineInvNumber() { return machineInvNumber; }
    public void setMachineInvNumber(String machineInvNumber) { this.machineInvNumber = machineInvNumber; }

    public String getMachineModel() { return machineModel; }
    public void setMachineModel(String machineModel) { this.machineModel = machineModel; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getMachineId() { return machineId; }
    public void setMachineId(Long machineId) { this.machineId = machineId; }

    public Long getTypeId() { return typeId; }
    public void setTypeId(Long typeId) { this.typeId = typeId; }
}