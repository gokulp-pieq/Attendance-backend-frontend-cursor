package com.attendance.service

import com.attendance.dao.AttendanceDAO
import com.attendance.dao.EmployeeDAO
import com.attendance.dto.CheckinRequest
import com.attendance.dto.CheckoutRequest
import com.attendance.dto.AttendanceSummaryResponse
import com.attendance.model.Attendance
import com.attendance.model.Employee
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Duration
import java.util.UUID
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response

class AttendanceService(
    private val attendanceDAO: AttendanceDAO,
    private val employeeDAO: EmployeeDAO
) {
    
    fun checkin(request: CheckinRequest): Attendance {
        // Validate employee exists
        val employee = employeeDAO.findByEmpId(request.empId)
            ?: throw WebApplicationException("Employee not found", Response.Status.NOT_FOUND)
        
        val checkinDate = request.checkinDatetime.toLocalDate()
        
        // Check if employee already has an open attendance for today
        val existingAttendances = attendanceDAO.findByEmployeeAndDate(request.empId, checkinDate.toString())
        val existingOpenAttendance = existingAttendances.find { it.checkoutDatetime == null }
        if (existingOpenAttendance != null) {
            throw WebApplicationException("Employee already checked in today", Response.Status.CONFLICT)
        }
        
        // Validate checkin time (business hours: 6 AM to 10 PM)
        validateCheckinTime(request.checkinDatetime)
        
        val id = attendanceDAO.createCheckin(request.empId, request.checkinDatetime)
        return Attendance(
            id = id,
            empId = request.empId,
            checkinDatetime = request.checkinDatetime
        )
    }
    
    fun checkout(request: CheckoutRequest): Attendance {
        // Validate employee exists
        val employee = employeeDAO.findByEmpId(request.empId)
            ?: throw WebApplicationException("Employee not found", Response.Status.NOT_FOUND)
        
        val checkoutDate = request.checkoutDatetime.toLocalDate()
        
        // Find open attendance for today
        val existingAttendances = attendanceDAO.findByEmployeeAndDate(request.empId, checkoutDate.toString())
        val openAttendance = existingAttendances.find { it.checkoutDatetime == null }
            ?: throw WebApplicationException("No open attendance found for today", Response.Status.NOT_FOUND)
        
        // Validate checkout time is after checkin time
        if (request.checkoutDatetime.isBefore(openAttendance.checkinDatetime)) {
            throw WebApplicationException("Checkout time cannot be before checkin time", Response.Status.BAD_REQUEST)
        }
        
        // Validate checkout time (business hours: 6 AM to 10 PM)
        validateCheckoutTime(request.checkoutDatetime)
        
        val rowsAffected = attendanceDAO.updateCheckout(request.empId, request.checkoutDatetime)
        if (rowsAffected == 0) {
            throw WebApplicationException("Failed to checkout", Response.Status.INTERNAL_SERVER_ERROR)
        }
        
        return openAttendance.copy(checkoutDatetime = request.checkoutDatetime)
    }
    
    fun getAttendanceByEmployeeId(empId: UUID): List<Attendance> {
        // Validate employee exists
        employeeDAO.findByEmpId(empId)
            ?: throw WebApplicationException("Employee not found", Response.Status.NOT_FOUND)
        
        return attendanceDAO.findByEmployee(empId)
    }
    
    fun getAttendanceByEmployeeIdAndDate(empId: UUID, date: LocalDate): List<Attendance> {
        // Validate employee exists
        employeeDAO.findByEmpId(empId)
            ?: throw WebApplicationException("Employee not found", Response.Status.NOT_FOUND)
        
        return attendanceDAO.findByEmployeeAndDate(empId, date.toString())
    }
    
    fun getAttendanceByDate(date: LocalDate): List<Attendance> {
        return attendanceDAO.findByDate(date.toString())
    }
    
    fun getAttendanceByDateRange(startDate: LocalDate, endDate: LocalDate): List<Attendance> {
        if (startDate.isAfter(endDate)) {
            throw WebApplicationException("Start date cannot be after end date", Response.Status.BAD_REQUEST)
        }
        
        return attendanceDAO.findByDateRange(startDate.toString(), endDate.toString())
    }
    
    fun getAttendanceSummaryByEmployeeId(empId: UUID, startDate: LocalDate, endDate: LocalDate): List<AttendanceSummaryResponse> {
        // Validate employee exists
        val employee = employeeDAO.findByEmpId(empId)
            ?: throw WebApplicationException("Employee not found", Response.Status.NOT_FOUND)
        
        if (startDate.isAfter(endDate)) {
            throw WebApplicationException("Start date cannot be after end date", Response.Status.BAD_REQUEST)
        }
        
        val attendances = attendanceDAO.findByDateRange(startDate.toString(), endDate.toString())
            .filter { it.empId == empId }
        
        return attendances.map { attendance ->
            createAttendanceSummaryResponse(attendance, employee)
        }
    }
    
    fun getAttendanceSummaryByDate(date: LocalDate): List<AttendanceSummaryResponse> {
        val attendances = attendanceDAO.findByDate(date.toString())
        val employeeIds = attendances.map { it.empId }.distinct()
        val employees = employeeIds.associateWith { employeeDAO.findByEmpId(it) }
        
        return attendances.mapNotNull { attendance ->
            employees[attendance.empId]?.let { employee ->
                createAttendanceSummaryResponse(attendance, employee)
            }
        }
    }
    
    private fun createAttendanceSummaryResponse(attendance: Attendance, employee: Employee): AttendanceSummaryResponse {
        val totalHours = attendance.checkoutDatetime?.let { checkout ->
            val duration = Duration.between(attendance.checkinDatetime, checkout)
            val hours = duration.toHours()
            val minutes = duration.toMinutesPart()
            "${hours}h ${minutes}m"
        }
        
        val status = when {
            attendance.checkoutDatetime == null -> "Checked In"
            else -> "Checked Out"
        }
        
        return AttendanceSummaryResponse(
            empId = attendance.empId.toString(),
            employeeName = "${employee.firstName} ${employee.lastName}",
            date = attendance.checkinDatetime.toLocalDate().toString(),
            checkinTime = attendance.checkinDatetime.toLocalTime().toString(),
            checkoutTime = attendance.checkoutDatetime?.toLocalTime()?.toString(),
            totalHours = totalHours,
            status = status
        )
    }
    
    private fun validateCheckinTime(checkinTime: LocalDateTime) {
        val time = checkinTime.toLocalTime()
        if (time.isBefore(LocalTime.of(6, 0)) || time.isAfter(LocalTime.of(22, 0))) {
            throw WebApplicationException("Checkin time must be between 6:00 AM and 10:00 PM", Response.Status.BAD_REQUEST)
        }
    }
    
    private fun validateCheckoutTime(checkoutTime: LocalDateTime) {
        val time = checkoutTime.toLocalTime()
        if (time.isBefore(LocalTime.of(6, 0)) || time.isAfter(LocalTime.of(22, 0))) {
            throw WebApplicationException("Checkout time must be between 6:00 AM and 10:00 PM", Response.Status.BAD_REQUEST)
        }
    }
}
