package com.unknownclinic.appointment.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * パスワードハッシュ生成用ユーティリティ
 * このクラスは開発時にパスワードのBCryptハッシュを生成するために使用します
 */
public class PasswordHashGenerator {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 管理者パスワード: admin123
        String adminPassword = "admin123";
        String adminHash = encoder.encode(adminPassword);
        System.out.println("管理者パスワード '" + adminPassword + "' のハッシュ:");
        System.out.println(adminHash);
        System.out.println();
        
        // 検証
        boolean matches = encoder.matches(adminPassword, adminHash);
        System.out.println("検証結果: " + matches);
    }
}

