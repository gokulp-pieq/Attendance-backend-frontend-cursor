package com.attendance.service

import com.attendance.dao.AttendanceDAO
import com.attendance.dao.EmployeeDAO
import com.attendance.dto.CheckinRequest
import com.attendance.dto.CheckoutRequest
import com.attendance.dto.AttendanceSummaryResponse
import com.attendance.dto.AttendanceResponse
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
    
    fun checkin(request: CheckinRequest): AttendanceResponse {
        // Validate employee exists
        employeeDAO.findByEmpId(request.empId)
            ?: throw WebApplicationException("Employee not found", Response.Status.NOT_FOUND)
        
        val checkinDate = request.checkinDatetime.toLocalDate()
        
        // Check if employee already has an open attendance for today
        val existingAttendances = attendanceDAO.findByEmployeeAndDate(request.empId, checkinDate.toString())
        val existingOpenAttendance = existingAttendances.find { it.checkoutDatetime == null }
        if (existingOpenAttendance != null) {
            throw WebApplicationException("Employee already checked in today. Must check out before checking in again.", Response.Status.CONFLICT)
        }
        
        // No time restrictions - allow check-in at any time
        
        attendanceDAO.createCheckin(request.empId, request.checkinDatetime)
        
        return AttendanceResponse(
            empId = request.empId.toString(),
            checkinDatetime = request.checkinDatetime,
            checkoutDatetime = null,
            totalWorkingSeconds = null
        )
    }
    
    fun checkout(request: CheckoutRequest): AttendanceResponse {
        // Validate employee exists
        employeeDAO.findByEmpId(request.empId)
            ?: throw WebApplicationException("Employee not found", Response.Status.NOT_FOUND)
        
        val checkoutDate = request.checkoutDatetime.toLocalDate()
        
        // Find open attendance for today
        val existingAttendances = attendanceDAO.findByEmployeeAndDate(request.empId, checkoutDate.toString())
        val openAttendance = existingAttendances.find { it.checkoutDatetime == null }
            ?: throw WebApplicationException("No open attendance found for today. Must check in before checking out.", Response.Status.NOT_FOUND)
        
        // Validate checkout time is after checkin time
        if (request.checkoutDatetime.isBefore(openAttendance.checkinDatetime)) {
            throw WebApplicationException("Checkout time cannot be before checkin time", Response.Status.BAD_REQUEST)
        }
        
        // No time restrictions - allow check-out at any time
        
        val rowsAffected = attendanceDAO.updateCheckout(request.empId, request.checkoutDatetime)
        if (rowsAffected == 0) {
            throw WebApplicationException("Failed to checkout", Response.Status.INTERNAL_SERVER_ERROR)
        }
        
        // Calculate total working hours in seconds
        val totalWorkingSeconds = Duration.between(openAttendance.checkinDatetime, request.checkoutDatetime).seconds
        
        return AttendanceResponse(
            empId = request.empId.toString(),
            checkinDatetime = openAttendance.checkinDatetime,
            checkoutDatetime = request.checkoutDatetime,
            totalWorkingSeconds = totalWorkingSeconds
        )
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
    
    /**
     * Get all attendance sessions for a specific day for an employee
     * This will show multiple check-ins/check-outs if they exist
     */
    fun getEmployeeDailyAttendanceSessions(empId: UUID, date: LocalDate): List<Attendance> {
        // Validate employee exists
        employeeDAO.findByEmpId(empId)
            ?: throw WebApplicationException("Employee not found", Response.Status.NOT_FOUND)
        
        return attendanceDAO.findByEmployeeAndDate(empId, date.toString())
            .sortedBy { it.checkinDatetime } // Sort by check-in time to show chronological order
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
    

    
    /**
     * Get the current attendance status for an employee
     * Returns the open attendance if employee is checked in, null if checked out
     */
    fun getCurrentAttendanceStatus(empId: UUID): Attendance? {
        // Validate employee exists
        employeeDAO.findByEmpId(empId)
            ?: throw WebApplicationException("Employee not found", Response.Status.NOT_FOUND)
        
        val today = LocalDate.now()
        val todayAttendances = attendanceDAO.findByEmployeeAndDate(empId, today.toString())
        return todayAttendances.find { it.checkoutDatetime == null }
    }
    
    /**
     * Calculate total working hours for a specific day for an employee
     * This handles multiple check-ins/check-outs per day
     */
    fun getEmployeeDailyWorkingHours(empId: UUID, date: LocalDate): Duration {
        // Validate employee exists
        employeeDAO.findByEmpId(empId)
            ?: throw WebApplicationException("Employee not found", Response.Status.NOT_FOUND)
        
        val dayAttendances = attendanceDAO.findByEmployeeAndDate(empId, date.toString())
            .filter { it.checkoutDatetime != null } // Only count completed sessions
            .sortedBy { it.checkinDatetime }
        
        var totalDuration = Duration.ZERO
        for (attendance in dayAttendances) {
            val sessionDuration = Duration.between(attendance.checkinDatetime, attendance.checkoutDatetime!!)
            totalDuration = totalDuration.plus(sessionDuration)
        }
        
        return totalDuration
    }
    
    /**
     * Calculate total working hours for an employee between two dates
     * This handles multiple check-ins/check-outs per day across the date range
     */
    fun getEmployeeWorkingHoursBetweenDates(empId: UUID, startDate: LocalDate, endDate: LocalDate): Duration {
        // Validate employee exists
        employeeDAO.findByEmpId(empId)
            ?: throw WebApplicationException("Employee not found", Response.Status.NOT_FOUND)
        
        if (startDate.isAfter(endDate)) {
            throw WebApplicationException("Start date cannot be after end date", Response.Status.BAD_REQUEST)
        }
        
        val attendances = attendanceDAO.findByEmployeeAndDateRange(empId, startDate.toString(), endDate.toString())
            .filter { it.checkoutDatetime != null } // Only count completed sessions
        
        var totalDuration = Duration.ZERO
        for (attendance in attendances) {
            val sessionDuration = Duration.between(attendance.checkinDatetime, attendance.checkoutDatetime!!)
            totalDuration = totalDuration.plus(sessionDuration)
        }
        
        return totalDuration
    }
}
