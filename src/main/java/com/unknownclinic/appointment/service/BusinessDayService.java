package com.unknownclinic.appointment.service;

import com.unknownclinic.appointment.domain.BusinessDay;
import com.unknownclinic.appointment.repository.BusinessDayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BusinessDayService {

    @Autowired
    private BusinessDayMapper businessDayMapper;

    public List<BusinessDay> getAllBusinessDays() {
        return businessDayMapper.findAllActive();
    }

    public BusinessDay findByDate(LocalDate date) {
        return businessDayMapper.findByDate(date);
    }

    @Transactional
    public void createBusinessDay(LocalDate date, String businessType) {
        BusinessDay existing = businessDayMapper.findByDate(date);
        if (existing != null) {
            throw new IllegalArgumentException("この日付は既に登録されています");
        }

        BusinessDay businessDay = new BusinessDay();
        businessDay.setBusinessDate(date);
        businessDay.setIsActive(true);
        businessDay.setBusinessType(businessType);
        businessDayMapper.insert(businessDay);
    }

    @Transactional
    public void updateBusinessDay(Long id, Boolean isActive, String businessType) {
        BusinessDay businessDay = businessDayMapper.findById(id);
        if (businessDay == null) {
            throw new IllegalArgumentException("営業日が見つかりません");
        }
        businessDay.setIsActive(isActive);
        businessDay.setBusinessType(businessType);
        businessDayMapper.update(businessDay);
    }

    @Transactional
    public void deleteBusinessDay(Long id) {
        businessDayMapper.delete(id);
    }
}

