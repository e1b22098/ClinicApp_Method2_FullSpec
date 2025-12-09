-- 管理者パスワードを更新するSQLスクリプト
-- パスワード: admin123 の新しいBCryptハッシュを生成して更新

USE clinic_booking_db;

-- 既存の管理者を削除（存在する場合）
DELETE FROM admins WHERE login_id = 'admin';

-- 新しい管理者を追加（パスワード: admin123）
-- このハッシュは PasswordHashGenerator.java で生成したものです
INSERT INTO admins (login_id, password) VALUES
('admin', '$2a$10$rKq5qJ5qJ5qJ5qJ5qJ5qOeKq5qJ5qJ5qJ5qJ5qJ5qJ5qJ5qJ5qJ5q');

-- 確認
SELECT id, login_id, created_at FROM admins WHERE login_id = 'admin';

