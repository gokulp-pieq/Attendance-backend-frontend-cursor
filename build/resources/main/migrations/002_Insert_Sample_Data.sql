-- Migration: Insert sample data
-- Description: Inserts sample employees and attendance records for testing

-- Insert sample employees
INSERT INTO employees (emp_id, first_name, last_name, email, role_id, dept_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 'John', 'Doe', 'john.doe@company.com', 1, 1),
    ('550e8400-e29b-41d4-a716-446655440002', 'Jane', 'Smith', 'jane.smith@company.com', 2, 1),
    ('550e8400-e29b-41d4-a716-446655440003', 'Bob', 'Johnson', 'bob.johnson@company.com', 1, 2),
    ('550e8400-e29b-41d4-a716-446655440004', 'Alice', 'Brown', 'alice.brown@company.com', 3, 3),
    ('550e8400-e29b-41d4-a716-446655440005', 'Charlie', 'Wilson', 'charlie.wilson@company.com', 1, 1);

-- Update reporting relationships
UPDATE employees SET reporting_to = '550e8400-e29b-41d4-a716-446655440002' 
WHERE emp_id IN ('550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440005');

UPDATE employees SET reporting_to = '550e8400-e29b-41d4-a716-446655440004' 
WHERE emp_id = '550e8400-e29b-41d4-a716-446655440003';

-- Insert sample attendance records (for today and yesterday)
INSERT INTO attendances (emp_id, checkin_datetime, checkout_datetime) VALUES
    ('550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '9 hours', CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '17 hours'),
    ('550e8400-e29b-41d4-a716-446655440002', CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '8 hours 30 minutes', CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '18 hours'),
    ('550e8400-e29b-41d4-a716-446655440003', CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '9 hours 15 minutes', CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '17 hours 30 minutes'),
    ('550e8400-e29b-41d4-a716-446655440004', CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '8 hours', CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '18 hours 30 minutes'),
    ('550e8400-e29b-41d4-a716-446655440005', CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '9 hours 45 minutes', CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '17 hours 15 minutes');

-- Insert today's check-ins (without checkout for some employees)
INSERT INTO attendances (emp_id, checkin_datetime) VALUES
    ('550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP - INTERVAL '2 hours'),
    ('550e8400-e29b-41d4-a716-446655440002', CURRENT_TIMESTAMP - INTERVAL '1 hour 30 minutes'),
    ('550e8400-e29b-41d4-a716-446655440003', CURRENT_TIMESTAMP - INTERVAL '3 hours'),
    ('550e8400-e29b-41d4-a716-446655440004', CURRENT_TIMESTAMP - INTERVAL '1 hour'),
    ('550e8400-e29b-41d4-a716-446655440005', CURRENT_TIMESTAMP - INTERVAL '45 minutes');
