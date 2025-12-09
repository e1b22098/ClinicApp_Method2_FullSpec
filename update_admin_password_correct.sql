-- 管理者パスワードを更新するSQL
-- パスワード: admin123

USE clinic_booking_db;

-- 既存の管理者を削除
DELETE FROM admins WHERE login_id = 'admin';

-- 新しい管理者を追加（パスワード: admin123）
-- このハッシュは BCryptPasswordEncoder で生成した "admin123" のハッシュです
INSERT INTO admins (login_id, password) VALUES
('admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi');

-- 確認
SELECT id, login_id, LENGTH(password) as pwd_length, SUBSTRING(password, 1, 7) as hash_prefix FROM admins WHERE login_id = 'admin';

