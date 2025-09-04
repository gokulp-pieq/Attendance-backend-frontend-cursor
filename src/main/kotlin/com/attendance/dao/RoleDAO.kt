package com.attendance.dao

import com.attendance.dto.RoleResponse
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.config.RegisterRowMapper
import com.attendance.db.RoleResponseRowMapper

interface RoleDAO {
    
    @SqlQuery("SELECT id, role_name FROM roles ORDER BY id")
    @RegisterRowMapper(RoleResponseRowMapper::class)
    fun findAll(): List<RoleResponse>
    
    @SqlQuery("SELECT id, role_name FROM roles WHERE id = :id")
    @RegisterRowMapper(RoleResponseRowMapper::class)
    fun findById(@Bind("id") id: Int): RoleResponse?
    
    @SqlQuery("SELECT id, role_name FROM roles WHERE role_name = :roleName")
    @RegisterRowMapper(RoleResponseRowMapper::class)
    fun findByRoleName(@Bind("roleName") roleName: String): RoleResponse?
}
