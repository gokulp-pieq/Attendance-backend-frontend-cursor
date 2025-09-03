package com.attendance.service

import com.attendance.dao.EmployeeDAO
import com.attendance.dto.LoginRequest
import com.attendance.dto.EmployeeResponse
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response

class AuthService(private val employeeDAO: EmployeeDAO) {

    fun login(request: LoginRequest): EmployeeResponse {
        val employee = employeeDAO.findByEmailWithDetails(request.email)
            ?: throw WebApplicationException("Invalid credentials", Response.Status.UNAUTHORIZED)

        // For now, we'll do a simple password check
        // In a real app, this would be hashed password comparison
        // We need to get the employee with password to check it
        val employeeWithPassword = employeeDAO.findByEmail(request.email)
        if (employeeWithPassword?.password != request.password) {
            throw WebApplicationException("Invalid credentials", Response.Status.UNAUTHORIZED)
        }

        return employee
    }
}
