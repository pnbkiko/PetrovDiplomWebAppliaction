package com.petrov.maintenance.dto;

import java.time.LocalDate;

public class MachineDTO {
    private Long id;
    private String model;
    private String invNumber;
    private LocalDate commissionedAt;

    // Конструкторы
    public MachineDTO() {}

    public MachineDTO(Long id, String model, String invNumber, LocalDate commissionedAt) {
        this.id = id;
        this.model = model;
        this.invNumber = invNumber;
        this.commissionedAt = commissionedAt;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getInvNumber() { return invNumber; }
    public void setInvNumber(String invNumber) { this.invNumber = invNumber; }

    public LocalDate getCommissionedAt() { return commissionedAt; }
    public void setCommissionedAt(LocalDate commissionedAt) { this.commissionedAt = commissionedAt; }
}