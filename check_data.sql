-- 初期データ確認用SQLスクリプト

USE clinic_booking_db;

-- 1. 時間枠の確認（14件登録されているはず）
SELECT 
    id,
    start_time,
    end_time,
    is_active,
    '時間枠' as type
FROM time_slots
ORDER BY start_time;

-- 2. 管理者の確認（1件登録されているはず）
SELECT 
    id,
    login_id,
    created_at,
    '管理者' as type
FROM admins;

-- 3. 各テーブルのレコード数を確認
SELECT 
    'admins' as table_name,
    COUNT(*) as record_count
FROM admins
UNION ALL
SELECT 
    'users' as table_name,
    COUNT(*) as record_count
FROM users
UNION ALL
SELECT 
    'business_days' as table_name,
    COUNT(*) as record_count
FROM business_days
UNION ALL
SELECT 
    'time_slots' as table_name,
    COUNT(*) as record_count
FROM time_slots
UNION ALL
SELECT 
    'bookings' as table_name,
    COUNT(*) as record_count
FROM bookings;

