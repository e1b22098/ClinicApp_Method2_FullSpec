package com.unknownclinic.appointment.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordHashGeneratorConfig {

    @Bean
    public CommandLineRunner generatePasswordHash() {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String adminPassword = "admin123";
            String adminHash = encoder.encode(adminPassword);
            
            System.out.println("========================================");
            System.out.println("管理者パスワード 'admin123' のハッシュ:");
            System.out.println(adminHash);
            System.out.println("========================================");
            System.out.println();
            System.out.println("SQLコマンド:");
            System.out.println("DELETE FROM admins WHERE login_id = 'admin';");
            System.out.println("INSERT INTO admins (login_id, password) VALUES ('admin', '" + adminHash + "');");
            System.out.println("========================================");
            
            // 検証
            boolean matches = encoder.matches(adminPassword, adminHash);
            System.out.println("検証結果: " + matches);
            System.out.println("========================================");
        };
    }
}

