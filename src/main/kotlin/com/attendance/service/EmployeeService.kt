package com.attendance.service

import com.attendance.dao.EmployeeDAO
import com.attendance.dto.CreateEmployeeRequest
import com.attendance.dto.UpdateEmployeeRequest
import com.attendance.model.Employee
import com.attendance.model.Role
import com.attendance.model.Department
import java.util.UUID
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response

class EmployeeService(private val employeeDAO: EmployeeDAO) {
    
    fun createEmployee(request: CreateEmployeeRequest): Employee {
        // Validate role and department
        validateRoleAndDepartment(request.roleId, request.deptId)
        
        // Check if email already exists
        if (employeeDAO.findByEmail(request.email) != null) {
            throw WebApplicationException("Email already exists", Response.Status.CONFLICT)
        }
        
        val employee = Employee(
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email,
            roleId = request.roleId,
            deptId = request.deptId,
            reportingTo = request.reportingTo
        )
        
        val id = employeeDAO.createEmployee(employee)
        return employee.copy(id = id)
    }
    
    fun getEmployeeByEmpId(empId: UUID): Employee {
        return employeeDAO.findByEmpId(empId) 
            ?: throw WebApplicationException("Employee not found", Response.Status.NOT_FOUND)
    }
    
    fun getEmployeeByEmail(email: String): Employee {
        return employeeDAO.findByEmail(email) 
            ?: throw WebApplicationException("Employee not found", Response.Status.NOT_FOUND)
    }
    
    fun getAllEmployees(): List<Employee> {
        return employeeDAO.findAll()
    }
    
    fun getEmployeesByDepartment(deptId: Int): List<Employee> {
        validateDepartment(deptId)
        return employeeDAO.findByDepartment(deptId)
    }
    
    fun getEmployeesByRole(roleId: Int): List<Employee> {
        validateRole(roleId)
        return employeeDAO.findByRole(roleId)
    }
    
    fun updateEmployee(empId: UUID, request: UpdateEmployeeRequest): Employee {
        val existingEmployee = employeeDAO.findByEmpId(empId)
            ?: throw WebApplicationException("Employee not found", Response.Status.NOT_FOUND)
        
        // Validate role and department
        validateRoleAndDepartment(request.roleId, request.deptId)
        
        // Check if email already exists for another employee
        if (request.email != existingEmployee.email && 
            employeeDAO.countByEmailExcludingId(request.email, empId) > 0) {
            throw WebApplicationException("Email already exists", Response.Status.CONFLICT)
        }
        
        val updatedEmployee = Employee(
            id = existingEmployee.id,
            empId = empId,
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email,
            roleId = request.roleId,
            deptId = request.deptId,
            reportingTo = request.reportingTo
        )
        
        val rowsAffected = employeeDAO.updateEmployee(updatedEmployee, empId)
        if (rowsAffected == 0) {
            throw WebApplicationException("Failed to update employee", Response.Status.INTERNAL_SERVER_ERROR)
        }
        
        return updatedEmployee
    }
    
    fun updateEmployeeByEmail(email: String, request: UpdateEmployeeRequest): Employee {
        val existingEmployee = employeeDAO.findByEmail(email)
            ?: throw WebApplicationException("Employee not found", Response.Status.NOT_FOUND)
        
        // Validate role and department
        validateRoleAndDepartment(request.roleId, request.deptId)
        
        // Check if email already exists for another employee
        if (request.email != existingEmployee.email && 
            employeeDAO.countByEmailExcludingId(request.email, existingEmployee.empId) > 0) {
            throw WebApplicationException("Email already exists", Response.Status.CONFLICT)
        }
        
        val updatedEmployee = Employee(
            id = existingEmployee.id,
            empId = existingEmployee.empId,
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email,
            roleId = request.roleId,
            deptId = request.deptId,
            reportingTo = request.reportingTo
        )
        
        val rowsAffected = employeeDAO.updateEmployee(updatedEmployee, existingEmployee.empId)
        if (rowsAffected == 0) {
            throw WebApplicationException("Failed to update employee", Response.Status.INTERNAL_SERVER_ERROR)
        }
        
        return updatedEmployee
    }
    
    fun deleteEmployee(empId: UUID): Boolean {
        val existingEmployee = employeeDAO.findByEmpId(empId)
            ?: throw WebApplicationException("Employee not found", Response.Status.NOT_FOUND)
        
        val rowsAffected = employeeDAO.deleteEmployee(empId)
        return rowsAffected > 0
    }
    
    fun deleteEmployeeByEmail(email: String): Boolean {
        val existingEmployee = employeeDAO.findByEmail(email)
            ?: throw WebApplicationException("Employee not found", Response.Status.NOT_FOUND)
        
        val rowsAffected = employeeDAO.deleteEmployee(existingEmployee.empId)
        return rowsAffected > 0
    }
    
    fun searchEmployees(query: String): List<Employee> {
        if (query.isBlank()) {
            return getAllEmployees()
        }
        
        return getAllEmployees().filter { employee ->
            employee.firstName.contains(query, ignoreCase = true) ||
            employee.lastName.contains(query, ignoreCase = true) ||
            employee.email.contains(query, ignoreCase = true)
        }
    }
    
    private fun validateRoleAndDepartment(roleId: Int, deptId: Int) {
        validateRole(roleId)
        validateDepartment(deptId)
    }
    
    private fun validateRole(roleId: Int) {
        if (Role.fromId(roleId) == null) {
            throw WebApplicationException("Invalid role ID: $roleId", Response.Status.BAD_REQUEST)
        }
    }
    
    private fun validateDepartment(deptId: Int) {
        if (Department.fromId(deptId) == null) {
            throw WebApplicationException("Invalid department ID: $deptId", Response.Status.BAD_REQUEST)
        }
    }
}
