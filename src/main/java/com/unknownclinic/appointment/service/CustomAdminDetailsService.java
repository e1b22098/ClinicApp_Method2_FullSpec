package com.unknownclinic.appointment.service;

import com.unknownclinic.appointment.domain.Admin;
import com.unknownclinic.appointment.repository.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomAdminDetailsService implements UserDetailsService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Admin admin = adminMapper.findByLoginId(loginId);
        if (admin == null) {
            throw new UsernameNotFoundException("管理者が見つかりません: " + loginId);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        return org.springframework.security.core.userdetails.User
            .withUsername(admin.getLoginId())
            .password(admin.getPassword())
            .authorities(authorities)
            .build();
    }
}

