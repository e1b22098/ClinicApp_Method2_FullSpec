package com.unknownclinic.appointment.service;

import com.unknownclinic.appointment.domain.User;
import com.unknownclinic.appointment.repository.UserMapper;
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
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String cardNumber) throws UsernameNotFoundException {
        User user = userMapper.findByCardNumber(cardNumber);
        if (user == null) {
            throw new UsernameNotFoundException("ユーザーが見つかりません: " + cardNumber);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return org.springframework.security.core.userdetails.User
            .withUsername(user.getCardNumber())
            .password(user.getPassword())
            .authorities(authorities)
            .build();
    }
}

