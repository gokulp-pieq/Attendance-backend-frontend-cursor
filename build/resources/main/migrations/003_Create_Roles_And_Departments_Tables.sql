-- Migration: Create roles and departments tables
-- Description: Creates roles and departments tables that are referenced by employees

-- Create roles table
CREATE TABLE IF NOT EXISTS roles (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE
);

-- Create departments table
CREATE TABLE IF NOT EXISTS departments (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    dept_name VARCHAR(100) NOT NULL UNIQUE
);

-- Insert default roles
INSERT INTO roles (role_name) VALUES
    ('Employee'),
    ('Supervisor'),
    ('Manager'),
    ('Director'),
    ('Administrator')
ON CONFLICT (role_name) DO NOTHING;

-- Insert default departments
INSERT INTO departments (dept_name) VALUES
    ('Information Technology'),
    ('Human Resources'),
    ('Finance'),
    ('Marketing'),
    ('Operations'),
    ('Engineering'),
    ('Support')
ON CONFLICT (dept_name) DO NOTHING;

-- Add foreign key constraints for employees table
ALTER TABLE employees ADD CONSTRAINT fk_employees_role_id 
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE RESTRICT;

ALTER TABLE employees ADD CONSTRAINT fk_employees_dept_id 
    FOREIGN KEY (dept_id) REFERENCES departments(id) ON DELETE RESTRICT;
