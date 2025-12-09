-- 管理者パスワードの確認用SQL

USE clinic_booking_db;

-- 管理者情報を確認
SELECT 
    id,
    login_id,
    LENGTH(password) as password_hash_length,
    SUBSTRING(password, 1, 7) as hash_prefix,
    created_at
FROM admins 
WHERE login_id = 'admin';

-- パスワードハッシュの形式確認
-- BCryptハッシュは通常 $2a$10$ で始まり、60文字の長さです

