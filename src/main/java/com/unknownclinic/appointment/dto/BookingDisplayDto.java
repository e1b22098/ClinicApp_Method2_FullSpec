package com.unknownclinic.appointment.dto;

import com.unknownclinic.appointment.domain.Booking;
import java.time.LocalDate;
import java.time.LocalTime;

public class BookingDisplayDto {
    private Long id;
    private LocalDate businessDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
    private String userName;
    private String userPhoneNumber;

    public BookingDisplayDto() {
    }

    public BookingDisplayDto(Booking booking) {
        this.id = booking.getId();
        this.businessDate = booking.getBusinessDate();
        this.startTime = booking.getStartTime();
        this.endTime = booking.getEndTime();
        this.status = booking.getStatus() != null ? booking.getStatus().name() : null;
        this.userName = booking.getUserName();
        this.userPhoneNumber = booking.getUserPhoneNumber();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }
}

