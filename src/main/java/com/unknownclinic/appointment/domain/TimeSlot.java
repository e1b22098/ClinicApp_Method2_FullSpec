package com.unknownclinic.appointment.domain;

import java.time.LocalTime;

public class TimeSlot {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isActive;

    public TimeSlot() {
    }

    public TimeSlot(Long id, LocalTime startTime, LocalTime endTime, Boolean isActive) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}

