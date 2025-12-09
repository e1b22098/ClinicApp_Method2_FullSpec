package com.unknownclinic.appointment.service;

import com.unknownclinic.appointment.domain.User;
import com.unknownclinic.appointment.repository.UserMapper;
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
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String cardNumber) throws UsernameNotFoundException {
        try {
            logger.debug("患者認証試行: cardNumber = {}", cardNumber);
            User user = userMapper.findByCardNumber(cardNumber);
            if (user == null) {
                logger.warn("ユーザーが見つかりません: cardNumber = {}", cardNumber);
                throw new UsernameNotFoundException("ユーザーが見つかりません: " + cardNumber);
            }

            logger.debug("ユーザーが見つかりました: cardNumber = {}, id = {}, name = {}", 
                user.getCardNumber(), user.getId(), user.getName());

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            return org.springframework.security.core.userdetails.User
                .withUsername(user.getCardNumber())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("患者認証中にエラーが発生しました: cardNumber = {}", cardNumber, e);
            throw new UsernameNotFoundException("患者認証中にエラーが発生しました: " + cardNumber, e);
        }
    }
}

