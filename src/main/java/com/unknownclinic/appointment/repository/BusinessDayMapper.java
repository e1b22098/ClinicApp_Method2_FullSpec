package com.unknownclinic.appointment.repository;

import com.unknownclinic.appointment.domain.BusinessDay;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface BusinessDayMapper {
    List<BusinessDay> findAllActive();
    BusinessDay findById(Long id);
    BusinessDay findByDate(LocalDate date);
    void insert(BusinessDay businessDay);
    void update(BusinessDay businessDay);
    void delete(Long id);
}

