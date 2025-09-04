package com.attendance.dao

import com.attendance.dto.DepartmentResponse
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.config.RegisterRowMapper
import com.attendance.db.DepartmentResponseRowMapper

interface DepartmentDAO {
    
    @SqlQuery("SELECT id, dept_name FROM departments ORDER BY id")
    @RegisterRowMapper(DepartmentResponseRowMapper::class)
    fun findAll(): List<DepartmentResponse>
    
    @SqlQuery("SELECT id, dept_name FROM departments WHERE id = :id")
    @RegisterRowMapper(DepartmentResponseRowMapper::class)
    fun findById(@Bind("id") id: Int): DepartmentResponse?
    
    @SqlQuery("SELECT id, dept_name FROM departments WHERE dept_name = :deptName")
    @RegisterRowMapper(DepartmentResponseRowMapper::class)
    fun findByDepartmentName(@Bind("deptName") deptName: String): DepartmentResponse?
}
