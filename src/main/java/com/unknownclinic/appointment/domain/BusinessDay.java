package com.unknownclinic.appointment.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BusinessDay {
    private Long id;
    private LocalDate businessDate;
    private Boolean isActive;
    private String businessType;
    private LocalDateTime createdAt;

    public BusinessDay() {
    }

    public BusinessDay(Long id, LocalDate businessDate, Boolean isActive, 
                      String businessType, LocalDateTime createdAt) {
        this.id = id;
        this.businessDate = businessDate;
        this.isActive = isActive;
        this.businessType = businessType;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(LocalDate businessDate) {
        this.businessDate = businessDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

