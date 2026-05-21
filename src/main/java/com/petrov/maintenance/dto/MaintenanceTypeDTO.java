package com.petrov.maintenance.dto;

public class MaintenanceTypeDTO {
    private Long id;
    private String name;
    private Integer intervalDays;

    // Конструкторы
    public MaintenanceTypeDTO() {}

    public MaintenanceTypeDTO(Long id, String name, Integer intervalDays) {
        this.id = id;
        this.name = name;
        this.intervalDays = intervalDays;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getIntervalDays() { return intervalDays; }
    public void setIntervalDays(Integer intervalDays) { this.intervalDays = intervalDays; }
}