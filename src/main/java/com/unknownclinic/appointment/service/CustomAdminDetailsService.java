package com.unknownclinic.appointment.service;

import com.unknownclinic.appointment.domain.Admin;
import com.unknownclinic.appointment.repository.AdminMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(CustomAdminDetailsService.class);

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        try {
            logger.debug("管理者認証試行: loginId = {}", loginId);
            Admin admin = adminMapper.findByLoginId(loginId);
            if (admin == null) {
                logger.warn("管理者が見つかりません: loginId = {}", loginId);
                throw new UsernameNotFoundException("管理者が見つかりません: " + loginId);
            }

            logger.debug("管理者が見つかりました: loginId = {}, id = {}", admin.getLoginId(), admin.getId());

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

            return org.springframework.security.core.userdetails.User
                .withUsername(admin.getLoginId())
                .password(admin.getPassword())
                .authorities(authorities)
                .build();
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("管理者認証中にエラーが発生しました: loginId = {}", loginId, e);
            throw new UsernameNotFoundException("管理者認証中にエラーが発生しました: " + loginId, e);
        }
    }
}

