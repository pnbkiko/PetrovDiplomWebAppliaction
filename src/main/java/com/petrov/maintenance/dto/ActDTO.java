package com.petrov.maintenance.dto;

import java.time.LocalDate;

public class ActDTO {
    private Long id;
    private Long machineId;
    private String machineInvNumber;
    private String machineModel;
    private Long typeId;
    private String typeName;
    private LocalDate date;
    private String engineer;
    private String notes;
    private Boolean signed;

    // Конструкторы
    public ActDTO() {}

    public ActDTO(Long id, Long machineId, String machineInvNumber, String machineModel,
                  Long typeId, String typeName, LocalDate date, String engineer,
                  String notes, Boolean signed) {
        this.id = id;
        this.machineId = machineId;
        this.machineInvNumber = machineInvNumber;
        this.machineModel = machineModel;
        this.typeId = typeId;
        this.typeName = typeName;
        this.date = date;
        this.engineer = engineer;
        this.notes = notes;
        this.signed = signed;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMachineId() { return machineId; }
    public void setMachineId(Long machineId) { this.machineId = machineId; }

    public String getMachineInvNumber() { return machineInvNumber; }
    public void setMachineInvNumber(String machineInvNumber) { this.machineInvNumber = machineInvNumber; }

    public String getMachineModel() { return machineModel; }
    public void setMachineModel(String machineModel) { this.machineModel = machineModel; }

    public Long getTypeId() { return typeId; }
    public void setTypeId(Long typeId) { this.typeId = typeId; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getEngineer() { return engineer; }
    public void setEngineer(String engineer) { this.engineer = engineer; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Boolean getSigned() { return signed; }
    public void setSigned(Boolean signed) { this.signed = signed; }
}