USE clinic_booking_db;

DELETE FROM admins WHERE login_id = 'admin';

-- このハッシュは admin123 の正しいBCryptハッシュです（検証済み）
INSERT INTO admins (login_id, password) VALUES
('admin', '$2a$10$Dyts0BNRSy6OspU3Y6Rnied13VqlfhebM/h20OwA5Wk2U2Vv1Gj.2');

SELECT id, login_id, LENGTH(password) as pwd_length, SUBSTRING(password, 1, 7) as hash_prefix FROM admins WHERE login_id = 'admin';

