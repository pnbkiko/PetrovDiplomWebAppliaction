// src/main/java/com/petrov/maintenance/model/Machine.java
package com.petrov.maintenance.model;

import javax.persistence.*;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "machines")
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", nullable = false)
    private MachineType machineType;

    @Column(nullable = false)
    private String model;

    @Column
    private String modification;

    @Column(nullable = false)
    private String manufacturer;

    @Column(name = "manufacturing_year")
    private Integer manufacturingYear;

    @Column(name = "serial_number", unique = true)
    private String serialNumber;

    @Column(name = "inv_number", nullable = false, unique = true)
    private String invNumber;

    @Column(name = "commissioned_at", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate commissionedAt;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MachineType getMachineType() { return machineType; }
    public void setMachineType(MachineType machineType) { this.machineType = machineType; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getModification() { return modification; }
    public void setModification(String modification) { this.modification = modification; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public Integer getManufacturingYear() { return manufacturingYear; }
    public void setManufacturingYear(Integer manufacturingYear) { this.manufacturingYear = manufacturingYear; }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public String getInvNumber() { return invNumber; }
    public void setInvNumber(String invNumber) { this.invNumber = invNumber; }

    public LocalDate getCommissionedAt() { return commissionedAt; }
    public void setCommissionedAt(LocalDate commissionedAt) { this.commissionedAt = commissionedAt; }

    // Вспомогательные методы для Thymeleaf
    @Transient
    public Long getTypeId() {
        return machineType != null ? machineType.getId() : null;
    }

    public void setTypeId(Long typeId) {
        if (this.machineType == null) {
            this.machineType = new MachineType();
        }
        this.machineType.setId(typeId);
    }

    @Transient
    public String getTypeName() {
        return machineType != null ? machineType.getName() : "";
    }

    @Transient
    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        if (getTypeName() != null && !getTypeName().isEmpty()) {
            sb.append(getTypeName()).append(" ");
        }
        sb.append(model);
        if (modification != null && !modification.isEmpty()) {
            sb.append(" (").append(modification).append(")");
        }
        if (manufacturingYear != null) {
            sb.append(" ").append(manufacturingYear).append(" г.");
        }
        return sb.toString();
    }
}