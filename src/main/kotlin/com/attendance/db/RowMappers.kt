package com.attendance.db

import com.attendance.model.Employee
import com.attendance.model.Attendance
import com.attendance.dto.EmployeeResponse
import com.attendance.dto.RoleResponse
import com.attendance.dto.DepartmentResponse
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.time.LocalDateTime
import java.util.UUID

class EmployeeRowMapper : RowMapper<Employee> {
    override fun map(rs: ResultSet, ctx: StatementContext): Employee {
        return Employee(
            id = rs.getInt("id"),
            empId = UUID.fromString(rs.getString("emp_id")),
            firstName = rs.getString("first_name"),
            lastName = rs.getString("last_name"),
            email = rs.getString("email"),
            password = rs.getString("password"),
            roleId = rs.getInt("role_id"),
            deptId = rs.getInt("dept_id"),
            reportingTo = rs.getString("reporting_to")?.let { UUID.fromString(it) }
        )
    }
}

class EmployeeResponseRowMapper : RowMapper<EmployeeResponse> {
    override fun map(rs: ResultSet, ctx: StatementContext): EmployeeResponse {
        return EmployeeResponse(
            id = rs.getInt("id"),
            empId = UUID.fromString(rs.getString("emp_id")),
            firstName = rs.getString("first_name"),
            lastName = rs.getString("last_name"),
            email = rs.getString("email"),
            roleId = rs.getInt("role_id"),
            roleName = rs.getString("role_name"),
            deptId = rs.getInt("dept_id"),
            deptName = rs.getString("dept_name"),
            reportingTo = rs.getString("reporting_to")?.let { UUID.fromString(it) }
        )
    }
}

class AttendanceRowMapper : RowMapper<Attendance> {
    override fun map(rs: ResultSet, ctx: StatementContext): Attendance {
        return Attendance(
            id = rs.getInt("id"),
            empId = UUID.fromString(rs.getString("emp_id")),
            checkinDatetime = rs.getTimestamp("checkin_datetime").toLocalDateTime(),
            checkoutDatetime = rs.getTimestamp("checkout_datetime")?.toLocalDateTime()
        )
    }
}

class RoleResponseRowMapper : RowMapper<RoleResponse> {
    override fun map(rs: ResultSet, ctx: StatementContext): RoleResponse {
        return RoleResponse(
            id = rs.getInt("id"),
            name = rs.getString("role_name")
        )
    }
}

class DepartmentResponseRowMapper : RowMapper<DepartmentResponse> {
    override fun map(rs: ResultSet, ctx: StatementContext): DepartmentResponse {
        return DepartmentResponse(
            id = rs.getInt("id"),
            name = rs.getString("dept_name")
        )
    }
}
