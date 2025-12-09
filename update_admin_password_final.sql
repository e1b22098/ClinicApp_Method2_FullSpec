USE clinic_booking_db;

DELETE FROM admins WHERE login_id = 'admin';

INSERT INTO admins (login_id, password) VALUES
('admin', '$2a$10$rKq5qJ5qJ5qJ5qJ5qJ5qOeKq5qJ5qJ5qJ5qJ5qJ5qJ5qJ5qJ5qJ5q');

SELECT id, login_id, LENGTH(password) as pwd_length, SUBSTRING(password, 1, 7) as hash_prefix FROM admins WHERE login_id = 'admin';

