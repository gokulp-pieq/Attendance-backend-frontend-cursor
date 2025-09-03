package com.attendance.resources

import com.attendance.dto.LoginRequest
import com.attendance.dto.LoginResponse
import com.attendance.dao.EmployeeDAO
import com.attendance.service.AuthService
import jakarta.validation.Valid
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.jdbi.v3.core.Jdbi

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class AuthResource(private val jdbi: Jdbi) {

    private val authService: AuthService by lazy {
        AuthService(jdbi.onDemand(EmployeeDAO::class.java))
    }

    @POST
    @Path("/login")
    fun login(@Valid request: LoginRequest): Response {
        val employee = authService.login(request)
        return Response.ok(employee).build()
    }
}
