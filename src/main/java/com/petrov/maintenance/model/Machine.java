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

    @Column(nullable = false)
    private String model;

    @Column(name = "inv_number", nullable = false, unique = true)
    private String invNumber;

    @Column(name = "commissioned_at", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate commissionedAt;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getInvNumber() { return invNumber; }
    public void setInvNumber(String invNumber) { this.invNumber = invNumber; }

    public LocalDate getCommissionedAt() { return commissionedAt; }
    public void setCommissionedAt(LocalDate commissionedAt) { this.commissionedAt = commissionedAt; }
}