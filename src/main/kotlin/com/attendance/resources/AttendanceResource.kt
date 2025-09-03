package com.attendance.resources

import com.attendance.dao.AttendanceDAO
import com.attendance.dao.EmployeeDAO
import com.attendance.dto.CheckinRequest
import com.attendance.dto.CheckoutRequest
import com.attendance.model.Attendance
import com.attendance.service.AttendanceService
import org.jdbi.v3.core.Jdbi
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

@Path("/api/attendance")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class AttendanceResource(private val jdbi: Jdbi) {
    
    private val attendanceService: AttendanceService by lazy {
        AttendanceService(
            jdbi.onDemand(AttendanceDAO::class.java),
            jdbi.onDemand(EmployeeDAO::class.java)
        )
    }
    
    @POST
    @Path("/checkin")
    fun checkin(@Valid request: CheckinRequest): Response {
        val attendance = attendanceService.checkin(request)
        return Response.status(Response.Status.CREATED).entity(attendance).build()
    }
    
    @POST
    @Path("/checkout")
    fun checkout(@Valid request: CheckoutRequest): Response {
        val attendance = attendanceService.checkout(request)
        return Response.ok(attendance).build()
    }
    
    @GET
    @Path("/employee/{empId}")
    fun getAttendanceByEmployeeId(@PathParam("empId") empId: String): Response {
        val uuid = try {
            UUID.fromString(empId)
        } catch (e: IllegalArgumentException) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(mapOf("error" to "Invalid UUID format")).build()
        }
        
        val attendances = attendanceService.getAttendanceByEmployeeId(uuid)
        return Response.ok(attendances).build()
    }
    
    @GET
    @Path("/employee/{empId}/date/{date}")
    fun getAttendanceByEmployeeIdAndDate(
        @PathParam("empId") empId: String,
        @PathParam("date") dateStr: String
    ): Response {
        val uuid = try {
            UUID.fromString(empId)
        } catch (e: IllegalArgumentException) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(mapOf("error" to "Invalid UUID format")).build()
        }
        
        val date = try {
            LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE)
        } catch (e: Exception) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(mapOf("error" to "Invalid date format. Use YYYY-MM-DD")).build()
        }
        
        val attendance = attendanceService.getAttendanceByEmployeeIdAndDate(uuid, date)
        return if (attendance != null) {
            Response.ok(attendance).build()
        } else {
            Response.status(Response.Status.NOT_FOUND)
                .entity(mapOf("error" to "No attendance found for the specified date")).build()
        }
    }
    
    @GET
    @Path("/date/{date}")
    fun getAttendanceByDate(@PathParam("date") dateStr: String): Response {
        val date = try {
            LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE)
        } catch (e: Exception) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(mapOf("error" to "Invalid date format. Use YYYY-MM-DD")).build()
        }
        
        val attendances = attendanceService.getAttendanceByDate(date)
        return Response.ok(attendances).build()
    }
    
    @GET
    @Path("/date-range")
    fun getAttendanceByDateRange(
        @QueryParam("start_date") startDateStr: String,
        @QueryParam("end_date") endDateStr: String
    ): Response {
        val startDate = try {
            LocalDate.parse(startDateStr, DateTimeFormatter.ISO_LOCAL_DATE)
        } catch (e: Exception) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(mapOf("error" to "Invalid start date format. Use YYYY-MM-DD")).build()
        }
        
        val endDate = try {
            LocalDate.parse(endDateStr, DateTimeFormatter.ISO_LOCAL_DATE)
        } catch (e: Exception) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(mapOf("error" to "Invalid end date format. Use YYYY-MM-DD")).build()
        }
        
        val attendances = attendanceService.getAttendanceByDateRange(startDate, endDate)
        return Response.ok(attendances).build()
    }
    
    @GET
    @Path("/summary/employee/{empId}")
    fun getAttendanceSummaryByEmployeeId(
        @PathParam("empId") empId: String,
        @QueryParam("start_date") startDateStr: String,
        @QueryParam("end_date") endDateStr: String
    ): Response {
        val uuid = try {
            UUID.fromString(empId)
        } catch (e: IllegalArgumentException) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(mapOf("error" to "Invalid UUID format")).build()
        }
        
        val startDate = try {
            LocalDate.parse(startDateStr, DateTimeFormatter.ISO_LOCAL_DATE)
        } catch (e: Exception) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(mapOf("error" to "Invalid start date format. Use YYYY-MM-DD")).build()
        }
        
        val endDate = try {
            LocalDate.parse(endDateStr, DateTimeFormatter.ISO_LOCAL_DATE)
        } catch (e: Exception) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(mapOf("error" to "Invalid end date format. Use YYYY-MM-DD")).build()
        }
        
        val summary = attendanceService.getAttendanceSummaryByEmployeeId(uuid, startDate, endDate)
        return Response.ok(summary).build()
    }
    
    @GET
    @Path("/summary/date/{date}")
    fun getAttendanceSummaryByDate(@PathParam("date") dateStr: String): Response {
        val date = try {
            LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE)
        } catch (e: Exception) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(mapOf("error" to "Invalid date format. Use YYYY-MM-DD")).build()
        }
        
        val summary = attendanceService.getAttendanceSummaryByDate(date)
        return Response.ok(summary).build()
    }
    
    @GET
    @Path("/today")
    fun getTodayAttendance(): Response {
        val today = LocalDate.now()
        val attendances = attendanceService.getAttendanceByDate(today)
        return Response.ok(attendances).build()
    }
    
    @GET
    @Path("/summary/today")
    fun getTodayAttendanceSummary(): Response {
        val today = LocalDate.now()
        val summary = attendanceService.getAttendanceSummaryByDate(today)
        return Response.ok(summary).build()
    }
}
