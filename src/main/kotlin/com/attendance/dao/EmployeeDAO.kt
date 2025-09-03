package com.attendance.dao

import com.attendance.model.Employee
import com.attendance.dto.EmployeeResponse
import com.attendance.db.EmployeeRowMapper
import com.attendance.db.EmployeeResponseRowMapper
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.jdbi.v3.sqlobject.statement.SqlScript
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.config.RegisterRowMapper
import java.util.UUID

@RegisterRowMapper(EmployeeRowMapper::class)
interface EmployeeDAO {
    
    @SqlUpdate("INSERT INTO employees (emp_id, first_name, last_name, email, password, role_id, dept_id, reporting_to) " +
               "VALUES (:empId, :firstName, :lastName, :email, :password, :roleId, :deptId, :reportingTo)")
    @GetGeneratedKeys
    fun createEmployee(@BindBean employee: Employee): Int
    
    @SqlQuery("SELECT * FROM employees WHERE id = :id")
    fun findById(@Bind("id") id: Int): Employee?
    
    @SqlQuery("SELECT * FROM employees WHERE emp_id = :empId")
    fun findByEmpId(@Bind("empId") empId: UUID): Employee?
    
    @SqlQuery("SELECT * FROM employees WHERE email = :email")
    fun findByEmail(@Bind("email") email: String): Employee?
    
    @SqlQuery("SELECT * FROM employees")
    fun findAll(): List<Employee>
    
    @SqlQuery("SELECT * FROM employees WHERE dept_id = :deptId")
    fun findByDepartment(@Bind("deptId") deptId: Int): List<Employee>
    
    @SqlQuery("SELECT * FROM employees WHERE role_id = :roleId")
    fun findByRole(@Bind("roleId") roleId: Int): List<Employee>
    
    @SqlUpdate("UPDATE employees SET first_name = :firstName, last_name = :lastName, " +
               "email = :email, role_id = :roleId, dept_id = :deptId, reporting_to = :reportingTo " +
               "WHERE emp_id = :empId")
    fun updateEmployee(@BindBean employee: Employee, @Bind("empId") empId: UUID): Int
    
    @SqlUpdate("DELETE FROM employees WHERE emp_id = :empId")
    fun deleteEmployee(@Bind("empId") empId: UUID): Int
    
    @SqlQuery("SELECT COUNT(*) FROM employees WHERE email = :email AND emp_id != :empId")
    fun countByEmailExcludingId(@Bind("email") email: String, @Bind("empId") empId: UUID): Int
    
    // New methods with role and department names
    @SqlQuery("""
        SELECT e.id, e.emp_id, e.first_name, e.last_name, e.email, e.role_id, e.dept_id, e.reporting_to,
               r.role_name, d.dept_name
        FROM employees e
        JOIN roles r ON e.role_id = r.id
        JOIN departments d ON e.dept_id = d.id
        WHERE e.id = :id
    """)
    @RegisterRowMapper(EmployeeResponseRowMapper::class)
    fun findByIdWithDetails(@Bind("id") id: Int): EmployeeResponse?
    
    @SqlQuery("""
        SELECT e.id, e.emp_id, e.first_name, e.last_name, e.email, e.role_id, e.dept_id, e.reporting_to,
               r.role_name, d.dept_name
        FROM employees e
        JOIN roles r ON e.role_id = r.id
        JOIN departments d ON e.dept_id = d.id
        WHERE e.emp_id = :empId
    """)
    @RegisterRowMapper(EmployeeResponseRowMapper::class)
    fun findByEmpIdWithDetails(@Bind("empId") empId: UUID): EmployeeResponse?
    
    @SqlQuery("""
        SELECT e.id, e.emp_id, e.first_name, e.last_name, e.email, e.role_id, e.dept_id, e.reporting_to,
               r.role_name, d.dept_name
        FROM employees e
        JOIN roles r ON e.role_id = r.id
        JOIN departments d ON e.dept_id = d.id
        WHERE e.email = :email
    """)
    @RegisterRowMapper(EmployeeResponseRowMapper::class)
    fun findByEmailWithDetails(@Bind("email") email: String): EmployeeResponse?
    
    @SqlQuery("""
        SELECT e.id, e.emp_id, e.first_name, e.last_name, e.email, e.role_id, e.dept_id, e.reporting_to,
               r.role_name, d.dept_name
        FROM employees e
        JOIN roles r ON e.role_id = r.id
        JOIN departments d ON e.dept_id = d.id
        ORDER BY e.first_name, e.last_name
    """)
    @RegisterRowMapper(EmployeeResponseRowMapper::class)
    fun findAllWithDetails(): List<EmployeeResponse>
    
    @SqlQuery("""
        SELECT e.id, e.emp_id, e.first_name, e.last_name, e.email, e.role_id, e.dept_id, e.reporting_to,
               r.role_name, d.dept_name
        FROM employees e
        JOIN roles r ON e.role_id = r.id
        JOIN departments d ON e.dept_id = d.id
        WHERE e.dept_id = :deptId
        ORDER BY e.first_name, e.last_name
    """)
    @RegisterRowMapper(EmployeeResponseRowMapper::class)
    fun findByDepartmentWithDetails(@Bind("deptId") deptId: Int): List<EmployeeResponse>
    
    @SqlQuery("""
        SELECT e.id, e.emp_id, e.first_name, e.last_name, e.email, e.role_id, e.dept_id, e.reporting_to,
               r.role_name, d.dept_name
        FROM employees e
        JOIN roles r ON e.role_id = r.id
        JOIN departments d ON e.dept_id = d.id
        WHERE e.role_id = :roleId
        ORDER BY e.first_name, e.last_name
    """)
    @RegisterRowMapper(EmployeeResponseRowMapper::class)
    fun findByRoleWithDetails(@Bind("roleId") roleId: Int): List<EmployeeResponse>
}
