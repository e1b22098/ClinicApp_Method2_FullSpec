-- データベース作成（手動で実行）
-- CREATE DATABASE clinic_booking_db;

USE clinic_booking_db;

-- 管理者テーブル
CREATE TABLE IF NOT EXISTS admins (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    login_id VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 患者テーブル
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_number VARCHAR(20) NOT NULL UNIQUE,
    birthday DATE NOT NULL,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 営業日テーブル
CREATE TABLE IF NOT EXISTS business_days (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    business_date DATE NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    business_type VARCHAR(50),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 時間枠テーブル
CREATE TABLE IF NOT EXISTS time_slots (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 予約テーブル
CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    business_date DATE NOT NULL,
    time_slot_id BIGINT NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (time_slot_id) REFERENCES time_slots(id) ON DELETE RESTRICT,
    UNIQUE KEY unique_booking (business_date, time_slot_id, user_id, status),
    INDEX idx_business_date (business_date),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 初期データ投入（時間枠）
INSERT INTO time_slots (start_time, end_time, is_active) VALUES
('09:00:00', '09:30:00', TRUE),
('09:30:00', '10:00:00', TRUE),
('10:00:00', '10:30:00', TRUE),
('10:30:00', '11:00:00', TRUE),
('11:00:00', '11:30:00', TRUE),
('11:30:00', '12:00:00', TRUE),
('13:00:00', '13:30:00', TRUE),
('13:30:00', '14:00:00', TRUE),
('14:00:00', '14:30:00', TRUE),
('14:30:00', '15:00:00', TRUE),
('15:00:00', '15:30:00', TRUE),
('15:30:00', '16:00:00', TRUE),
('16:00:00', '16:30:00', TRUE),
('16:30:00', '17:00:00', TRUE);

-- テスト用管理者（パスワード: admin123）
INSERT INTO admins (login_id, password) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pJwC');

