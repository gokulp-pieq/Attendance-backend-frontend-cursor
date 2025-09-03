package com.attendance.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class UpdateEmployeeRequest(
    @field:NotBlank(message = "First name is required")
    @JsonProperty("first_name")
    val firstName: String,
    
    @field:NotBlank(message = "Last name is required")
    @JsonProperty("last_name")
    val lastName: String,
    
    @field:Email(message = "Invalid email format")
    @field:NotBlank(message = "Email is required")
    val email: String,
    
    @field:NotNull(message = "Role ID is required")
    @JsonProperty("role_id")
    val roleId: Int,
    
    @field:NotNull(message = "Department ID is required")
    @JsonProperty("dept_id")
    val deptId: Int,
    
    @JsonProperty("reporting_to")
    val reportingTo: UUID? = null
)
