-- Migration: Create initial tables
-- Description: Creates employees and attendances tables

-- Create employees table
CREATE TABLE IF NOT EXISTS employees (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    emp_id UUID DEFAULT gen_random_uuid(),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    role_id INT NOT NULL,
    dept_id INT NOT NULL,
    reporting_to UUID
);

-- Create attendances table
CREATE TABLE IF NOT EXISTS attendances(
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    emp_id UUID NOT NULL,
    checkin_datetime TIMESTAMP NOT NULL,
    checkout_datetime TIMESTAMP NULL
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_employees_emp_id ON employees(emp_id);
CREATE INDEX IF NOT EXISTS idx_employees_email ON employees(email);
CREATE INDEX IF NOT EXISTS idx_employees_dept_id ON employees(dept_id);
CREATE INDEX IF NOT EXISTS idx_employees_role_id ON employees(role_id);

CREATE INDEX IF NOT EXISTS idx_attendances_emp_id ON attendances(emp_id);
CREATE INDEX IF NOT EXISTS idx_attendances_checkin_date ON attendances(DATE(checkin_datetime));
CREATE INDEX IF NOT EXISTS idx_attendances_checkout_date ON attendances(DATE(checkout_datetime));

-- Add foreign key constraint for reporting_to
ALTER TABLE employees ADD CONSTRAINT fk_employees_reporting_to 
    FOREIGN KEY (reporting_to) REFERENCES employees(emp_id) ON DELETE SET NULL;

-- Add foreign key constraint for attendances.emp_id
ALTER TABLE attendances ADD CONSTRAINT fk_attendances_emp_id 
    FOREIGN KEY (emp_id) REFERENCES employees(emp_id) ON DELETE CASCADE;
