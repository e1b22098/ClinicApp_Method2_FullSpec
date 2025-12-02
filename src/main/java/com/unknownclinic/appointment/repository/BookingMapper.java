package com.unknownclinic.appointment.repository;

import com.unknownclinic.appointment.domain.Booking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface BookingMapper {
    Booking findById(Long id);
    List<Booking> findByUserId(Long userId);
    List<Booking> findByBusinessDate(LocalDate businessDate);
    List<Booking> findByBusinessDateAndTimeSlot(@Param("businessDate") LocalDate businessDate, @Param("timeSlotId") Long timeSlotId);
    Booking findByUserAndDateAndTimeSlot(@Param("userId") Long userId, @Param("businessDate") LocalDate businessDate, @Param("timeSlotId") Long timeSlotId);
    void insert(Booking booking);
    void update(Booking booking);
    void cancel(Long id);
}

