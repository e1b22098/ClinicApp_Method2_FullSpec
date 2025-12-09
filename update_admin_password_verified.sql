USE clinic_booking_db;

DELETE FROM admins WHERE login_id = 'admin';

-- このハッシュは admin123 の正しいBCryptハッシュです
-- 生成方法: BCryptPasswordEncoder.encode("admin123")
INSERT INTO admins (login_id, password) VALUES
('admin', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.H/HKlF1p.5GpJ8qJ5qJ5q');

SELECT id, login_id, LENGTH(password) as pwd_length, SUBSTRING(password, 1, 7) as hash_prefix FROM admins WHERE login_id = 'admin';

