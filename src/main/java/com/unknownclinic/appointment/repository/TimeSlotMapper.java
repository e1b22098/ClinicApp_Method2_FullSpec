package com.unknownclinic.appointment.repository;

import com.unknownclinic.appointment.domain.TimeSlot;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TimeSlotMapper {
    List<TimeSlot> findAllActive();
    TimeSlot findById(Long id);
}

