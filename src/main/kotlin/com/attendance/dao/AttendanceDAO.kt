package com.attendance.dao

import com.attendance.model.Attendance
import com.attendance.db.AttendanceRowMapper
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.config.RegisterRowMapper
import java.time.LocalDateTime
import java.util.UUID

@RegisterRowMapper(AttendanceRowMapper::class)
interface AttendanceDAO {
    
    @SqlUpdate("INSERT INTO attendances (emp_id, checkin_datetime) VALUES (:empId, :checkinDatetime)")
    @GetGeneratedKeys
    fun createCheckin(@Bind("empId") empId: UUID, @Bind("checkinDatetime") checkinDatetime: LocalDateTime): Int
    
    @SqlUpdate("UPDATE attendances SET checkout_datetime = :checkoutDatetime WHERE emp_id = :empId AND checkout_datetime IS NULL")
    fun updateCheckout(@Bind("empId") empId: UUID, @Bind("checkoutDatetime") checkoutDatetime: LocalDateTime): Int
    
    @SqlQuery("SELECT * FROM attendances WHERE emp_id = :empId ORDER BY checkin_datetime DESC")
    fun findByEmployee(@Bind("empId") empId: UUID): List<Attendance>
    
    @SqlQuery("SELECT * FROM attendances WHERE DATE(checkin_datetime) = :date::date")
    fun findByDate(@Bind("date") date: String): List<Attendance>
    
    @SqlQuery("SELECT * FROM attendances WHERE emp_id = :empId AND DATE(checkin_datetime) = :date::date")
    fun findByEmployeeAndDate(@Bind("empId") empId: UUID, @Bind("date") date: String): List<Attendance>
    
    @SqlQuery("SELECT * FROM attendances WHERE DATE(checkin_datetime) = CURRENT_DATE")
    fun findToday(): List<Attendance>
    
    @SqlQuery("SELECT * FROM attendances WHERE DATE(checkin_datetime) BETWEEN :startDate::date AND :endDate::date")
    fun findByDateRange(@Bind("startDate") startDate: String, @Bind("endDate") endDate: String): List<Attendance>
}
