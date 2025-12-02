package com.unknownclinic.appointment.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class BookingFormDto {
    @NotNull(message = "営業日を選択してください")
    private LocalDate businessDate;

    @NotNull(message = "時間枠を選択してください")
    private Long timeSlotId;

    public BookingFormDto() {
    }

    public BookingFormDto(LocalDate businessDate, Long timeSlotId) {
        this.businessDate = businessDate;
        this.timeSlotId = timeSlotId;
    }

    public LocalDate getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(LocalDate businessDate) {
        this.businessDate = businessDate;
    }

    public Long getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(Long timeSlotId) {
        this.timeSlotId = timeSlotId;
    }
}

