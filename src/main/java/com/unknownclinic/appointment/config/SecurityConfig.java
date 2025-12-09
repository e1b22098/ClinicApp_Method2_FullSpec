package com.unknownclinic.appointment.config;

import com.unknownclinic.appointment.service.CustomUserDetailsService;
import com.unknownclinic.appointment.service.CustomAdminDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private CustomAdminDetailsService adminDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher(request -> !request.getRequestURI().startsWith("/admin"))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(
                    "/login",
                    "/logout",
                    "/register",
                    "/register/**",
                    "/",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/error")
                .permitAll()
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/appointments", true)
                .failureUrl("/login?error"))
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout"))
            .userDetailsService(userDetailsService)
            .csrf(csrf -> csrf.ignoringRequestMatchers("/login", "/register"));

        return http.build();
    }
}

