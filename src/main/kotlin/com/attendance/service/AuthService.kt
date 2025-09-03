package com.attendance.service

import com.attendance.dao.EmployeeDAO
import com.attendance.dto.LoginRequest
import com.attendance.dto.LoginResponse
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response

class AuthService(private val employeeDAO: EmployeeDAO) {

    fun login(request: LoginRequest): LoginResponse {
        val employee = employeeDAO.findByEmail(request.email)
            ?: throw WebApplicationException("Invalid credentials", Response.Status.UNAUTHORIZED)

        if (employee.password != request.password) { // In a real app, this would be hashed password comparison
            throw WebApplicationException("Invalid credentials", Response.Status.UNAUTHORIZED)
        }

        return LoginResponse(
            id = employee.id!!,
            empId = employee.empId,
            firstName = employee.firstName,
            lastName = employee.lastName,
            email = employee.email,
            roleId = employee.roleId,
            deptId = employee.deptId,
            reportingTo = employee.reportingTo
        )
    }
}
