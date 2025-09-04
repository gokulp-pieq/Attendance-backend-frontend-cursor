package com.attendance.service

import com.attendance.dao.EmployeeDAO
import com.attendance.dao.RoleDAO
import com.attendance.dao.DepartmentDAO
import com.attendance.dto.CreateEmployeeRequest
import com.attendance.dto.UpdateEmployeeRequest
import com.attendance.dto.EmployeeResponse
import com.attendance.dto.RoleResponse
import com.attendance.dto.DepartmentResponse
import com.attendance.model.Employee
import java.util.UUID
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response

class EmployeeService(
    private val employeeDAO: EmployeeDAO,
    private val roleDAO: RoleDAO,
    private val departmentDAO: DepartmentDAO
) {
    
    fun createEmployee(request: CreateEmployeeRequest): Employee {
        // Look up role and department IDs from names
        val role = roleDAO.findByRoleName(request.roleName)
            ?: throw WebApplicationException("Invalid role name: ${request.roleName}", Response.Status.BAD_REQUEST)
        
        val department = departmentDAO.findByDepartmentName(request.deptName)
            ?: throw WebApplicationException("Invalid department name: ${request.deptName}", Response.Status.BAD_REQUEST)
        
        val roleId = role.id
        val deptId = department.id
        
        // Validate role and department
        validateRoleAndDepartment(roleId, deptId)
        
        // Check if email already exists
        if (employeeDAO.findByEmail(request.email) != null) {
            throw WebApplicationException("Email already exists", Response.Status.CONFLICT)
        }
        
        // Convert reporting_to string to UUID and validate it exists
        val reportingToUuid = request.reportingTo?.let { reportingToStr ->
            try {
                val uuid = UUID.fromString(reportingToStr)
                // Validate that the reporting manager exists
                if (employeeDAO.findByEmpId(uuid) == null) {
                    throw WebApplicationException("Reporting manager with UUID $reportingToStr not found", Response.Status.BAD_REQUEST)
                }
                uuid
            } catch (e: IllegalArgumentException) {
                throw WebApplicationException("Invalid UUID format for reporting_to: $reportingToStr", Response.Status.BAD_REQUEST)
            }
        }
        
        val employee = Employee(
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email,
            password = request.password,
            roleId = roleId,
            deptId = deptId,
            reportingTo = reportingToUuid
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
    
    // New methods that return EmployeeResponse with role and department names
    fun getEmployeeByEmpIdWithDetails(empId: UUID): EmployeeResponse {
        return employeeDAO.findByEmpIdWithDetails(empId) 
            ?: throw WebApplicationException("Employee not found", Response.Status.NOT_FOUND)
    }
    
    fun getEmployeeByEmailWithDetails(email: String): EmployeeResponse {
        return employeeDAO.findByEmailWithDetails(email) 
            ?: throw WebApplicationException("Employee not found", Response.Status.NOT_FOUND)
    }
    
    fun getAllEmployeesWithDetails(): List<EmployeeResponse> {
        return employeeDAO.findAllWithDetails()
    }
    
    fun getEmployeesByDepartmentWithDetails(deptId: Int): List<EmployeeResponse> {
        validateDepartment(deptId)
        return employeeDAO.findByDepartmentWithDetails(deptId)
    }
    
    fun getEmployeesByRoleWithDetails(roleId: Int): List<EmployeeResponse> {
        validateRole(roleId)
        return employeeDAO.findByRoleWithDetails(roleId)
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
            password = existingEmployee.password,
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
            password = existingEmployee.password,
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
    
    fun getRoles(): List<RoleResponse> {
        return roleDAO.findAll()
    }
    
    fun getDepartments(): List<DepartmentResponse> {
        return departmentDAO.findAll()
    }
    
    private fun validateRoleAndDepartment(roleId: Int, deptId: Int) {
        validateRole(roleId)
        validateDepartment(deptId)
    }
    
    private fun validateRole(roleId: Int) {
        if (roleDAO.findById(roleId) == null) {
            throw WebApplicationException("Invalid role ID: $roleId", Response.Status.BAD_REQUEST)
        }
    }
    
    private fun validateDepartment(deptId: Int) {
        if (departmentDAO.findById(deptId) == null) {
            throw WebApplicationException("Invalid department ID: $deptId", Response.Status.BAD_REQUEST)
        }
    }
}
