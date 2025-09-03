package com.attendance.dao

import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.customizer.Bind

interface DepartmentDAO {
    
    @SqlQuery("SELECT id, dept_name FROM departments ORDER BY id")
    fun findAll(): List<Map<String, Any>>
    
    @SqlQuery("SELECT id, dept_name FROM departments WHERE id = :id")
    fun findById(@Bind("id") id: Int): Map<String, Any>?
    
    @SqlQuery("SELECT id, dept_name FROM departments WHERE dept_name = :deptName")
    fun findByDepartmentName(@Bind("deptName") deptName: String): Map<String, Any>?
}
