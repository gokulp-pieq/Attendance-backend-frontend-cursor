package com.attendance.dao

import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.customizer.Bind

interface RoleDAO {
    
    @SqlQuery("SELECT id, role_name FROM roles ORDER BY id")
    fun findAll(): List<Map<String, Any>>
    
    @SqlQuery("SELECT id, role_name FROM roles WHERE id = :id")
    fun findById(@Bind("id") id: Int): Map<String, Any>?
    
    @SqlQuery("SELECT id, role_name FROM roles WHERE role_name = :roleName")
    fun findByRoleName(@Bind("roleName") roleName: String): Map<String, Any>?
}
