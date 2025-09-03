package com.attendance.resources

import com.attendance.dao.EmployeeDAO
import com.attendance.dao.RoleDAO
import com.attendance.dao.DepartmentDAO
import com.attendance.dto.CreateEmployeeRequest
import com.attendance.dto.UpdateEmployeeRequest
import com.attendance.dto.EmployeeResponse
import com.attendance.model.Employee
import com.attendance.service.EmployeeService
import org.jdbi.v3.core.Jdbi
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import java.util.UUID

@Path("/api/employees")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class EmployeeResource(private val jdbi: Jdbi) {
    
    private val employeeService: EmployeeService by lazy {
        EmployeeService(
            jdbi.onDemand(EmployeeDAO::class.java),
            jdbi.onDemand(RoleDAO::class.java),
            jdbi.onDemand(DepartmentDAO::class.java)
        )
    }
    
    @POST
    fun createEmployee(@Valid request: CreateEmployeeRequest): Response {
        val employee = employeeService.createEmployee(request)
        return Response.status(Response.Status.CREATED).entity(employee).build()
    }
    
    @GET
    fun getAllEmployees(
        @QueryParam("dept_id") deptId: Int?,
        @QueryParam("role_id") roleId: Int?,
        @QueryParam("search") search: String?
    ): Response {
        val employees = when {
            deptId != null -> employeeService.getEmployeesByDepartmentWithDetails(deptId)
            roleId != null -> employeeService.getEmployeesByRoleWithDetails(roleId)
            !search.isNullOrBlank() -> {
                // For search, we still use the old method but convert to EmployeeResponse
                val searchResults = employeeService.searchEmployees(search)
                searchResults.map { employee ->
                    // Convert Employee to EmployeeResponse using DAO values
                    val role = jdbi.onDemand(RoleDAO::class.java).findById(employee.roleId)
                    val dept = jdbi.onDemand(DepartmentDAO::class.java).findById(employee.deptId)
                    EmployeeResponse(
                        id = employee.id,
                        empId = employee.empId,
                        firstName = employee.firstName,
                        lastName = employee.lastName,
                        email = employee.email,
                        roleId = employee.roleId,
                        roleName = role?.get("role_name")?.toString() ?: "Unknown",
                        deptId = employee.deptId,
                        deptName = dept?.get("dept_name")?.toString() ?: "Unknown",
                        reportingTo = employee.reportingTo
                    )
                }
            }
            else -> employeeService.getAllEmployeesWithDetails()
        }
        
        return Response.ok(employees).build()
    }
    
    @GET
    @Path("/uuid/{empId}")
    fun getEmployeeByUuid(@PathParam("empId") empId: String): Response {
        val uuid = try {
            UUID.fromString(empId)
        } catch (e: IllegalArgumentException) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(mapOf("error" to "Invalid UUID format")).build()
        }
        
        val employee = employeeService.getEmployeeByEmpIdWithDetails(uuid)
        return Response.ok(employee).build()
    }
    
    @GET
    @Path("/email/{email}")
    fun getEmployeeByEmail(@PathParam("email") email: String): Response {
        val employee = employeeService.getEmployeeByEmailWithDetails(email)
        return Response.ok(employee).build()
    }
    
    @PUT
    @Path("/uuid/{empId}")
    fun updateEmployeeByUuid(
        @PathParam("empId") empId: String,
        @Valid request: UpdateEmployeeRequest
    ): Response {
        val uuid = try {
            UUID.fromString(empId)
        } catch (e: IllegalArgumentException) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(mapOf("error" to "Invalid UUID format")).build()
        }
        
        val employee = employeeService.updateEmployee(uuid, request)
        return Response.ok(employee).build()
    }
    
    @PUT
    @Path("/email/{email}")
    fun updateEmployeeByEmail(
        @PathParam("email") email: String,
        @Valid request: UpdateEmployeeRequest
    ): Response {
        val employee = employeeService.updateEmployeeByEmail(email, request)
        return Response.ok(employee).build()
    }
    
    @DELETE
    @Path("/uuid/{empId}")
    fun deleteEmployeeByUuid(@PathParam("empId") empId: String): Response {
        val uuid = try {
            UUID.fromString(empId)
        } catch (e: IllegalArgumentException) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(mapOf("error" to "Invalid UUID format")).build()
        }
        
        val deleted = employeeService.deleteEmployee(uuid)
        return if (deleted) {
            Response.noContent().build()
        } else {
            Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(mapOf("error" to "Failed to delete employee")).build()
        }
    }
    
    @DELETE
    @Path("/email/{email}")
    fun deleteEmployeeByEmail(@PathParam("email") email: String): Response {
        val deleted = employeeService.deleteEmployeeByEmail(email)
        return if (deleted) {
            Response.noContent().build()
        } else {
            Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(mapOf("error" to "Failed to delete employee")).build()
        }
    }
    
    @GET
    @Path("/roles")
    fun getRoles(): Response {
        val roles = jdbi.onDemand(RoleDAO::class.java).findAll().map { role ->
            mapOf(
                "id" to role["id"],
                "name" to role["role_name"]
            )
        }
        return Response.ok(roles).build()
    }
    
    @GET
    @Path("/departments")
    fun getDepartments(): Response {
        val departments = jdbi.onDemand(DepartmentDAO::class.java).findAll().map { dept ->
            mapOf(
                "id" to dept["id"],
                "name" to dept["dept_name"]
            )
        }
        return Response.ok(departments).build()
    }
}
