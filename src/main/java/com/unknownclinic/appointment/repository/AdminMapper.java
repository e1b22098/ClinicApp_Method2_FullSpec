package com.unknownclinic.appointment.repository;

import com.unknownclinic.appointment.domain.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
    Admin findByLoginId(String loginId);
}

