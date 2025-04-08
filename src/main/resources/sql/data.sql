-- Default admin user (password: 'adminpassword')
INSERT INTO users (username, password_hash, email, phone_number, address, role)
VALUES 
    ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMy.Mrq4L0FpX37W5Q7sR7BtJ7tW6W7NEyO', 'admin@gym.com', '+0000000000', 'Admin Office', 'ADMIN')
ON CONFLICT (username) DO NOTHING;

-- Sample trainers (password: 'trainerpassword')
INSERT INTO users (username, password_hash, email, phone_number, address, role)
VALUES 
    ('trainer1', '$2a$10$N9qo8uLOickgx2ZMRZoMy.Mrq4L0FpX37W5Q7sR7BtJ7tW6W7NEyO', 'trainer1@gym.com', '+1234567890', '123 Fitness St', 'TRAINER'),
    ('trainer2', '$2a$10$N9qo8uLOickgx2ZMRZoMy.Mrq4L0FpX37W5Q7sR7BtJ7tW6W7NEyO', 'trainer2@gym.com', '+1234567891', '456 Workout Ave', 'TRAINER')
ON CONFLICT (username) DO NOTHING;

-- Sample members (password: 'memberpassword')
INSERT INTO users (username, password_hash, email, phone_number, address, role)
VALUES 
    ('member1', '$2a$10$N9qo8uLOickgx2ZMRZoMy.Mrq4L0FpX37W5Q7sR7BtJ7tW6W7NEyO', 'member1@gym.com', '+9876543210', '789 Healthy Rd', 'MEMBER'),
    ('member2', '$2a$10$N9qo8uLOickgx2ZMRZoMy.Mrq4L0FpX37W5Q7sR7BtJ7tW6W7NEyO', 'member2@gym.com', '+9876543211', '321 Wellness Blvd', 'MEMBER'),
    ('member3', '$2a$10$N9qo8uLOickgx2ZMRZoMy.Mrq4L0FpX37W5Q7sR7BtJ7tW6W7NEyO', 'member3@gym.com', '+9876543212', '654 Strong Lane', 'MEMBER')
ON CONFLICT (username) DO NOTHING;

-- Sample memberships
INSERT INTO memberships (user_id, type, description, start_date, end_date, price, payment_status)
VALUES 
    (3, 'PREMIUM', 'Monthly premium membership with access to all classes', CURRENT_DATE, CURRENT_DATE + INTERVAL '1 month', 49.99, 'PAID'),
    (4, 'BASIC', 'Basic membership with limited access', CURRENT_DATE, CURRENT_DATE + INTERVAL '1 month', 29.99, 'PAID'),
    (5, 'PLATINUM', 'Platinum membership with personal training sessions', CURRENT_DATE, CURRENT_DATE + INTERVAL '3 months', 99.99, 'PAID')
ON CONFLICT DO NOTHING;

-- Sample workout classes
INSERT INTO workout_classes (name, description, type, trainer_id, schedule, duration_minutes, max_capacity)
VALUES 
    ('Morning Yoga', 'Beginner-friendly yoga class', 'YOGA', 2, CURRENT_DATE + INTERVAL '1 day 07:00:00', 60, 20),
    ('HIIT Burn', 'High-intensity interval training for calorie burning', 'HIIT', 2, CURRENT_DATE + INTERVAL '2 day 08:00:00', 45, 15),
    ('Strength Training', 'Build muscle and improve strength with weights', 'STRENGTH', 2, CURRENT_DATE + INTERVAL '3 day 17:30:00', 60, 25),
    ('Spin Class', 'High-energy indoor cycling session', 'CYCLING', 3, CURRENT_DATE + INTERVAL '4 day 07:00:00', 50, 30),
    ('Zumba Dance', 'Dance your way to fitness with Zumba', 'DANCE', 3, CURRENT_DATE + INTERVAL '5 day 18:00:00', 60, 20),
    ('Pilates', 'Core strengthening and flexibility improvement', 'PILATES', 2, CURRENT_DATE + INTERVAL '6 day 10:00:00', 60, 15)
ON CONFLICT DO NOTHING;

-- Sample enrollments
INSERT INTO class_enrollments (member_id, class_id)
VALUES 
    (3, 1),  -- Member 1 enrolled in Morning Yoga
    (3, 2),  -- Member 1 enrolled in HIIT Burn
    (4, 3),  -- Member 2 enrolled in Strength Training
    (5, 4),  -- Member 3 enrolled in Spin Class
    (5, 5)   -- Member 3 enrolled in Zumba Dance
ON CONFLICT DO NOTHING;
