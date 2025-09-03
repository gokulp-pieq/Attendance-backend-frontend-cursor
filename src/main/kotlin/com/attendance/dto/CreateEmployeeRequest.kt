package com.attendance.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateEmployeeRequest @JsonCreator constructor(
    @field:NotBlank(message = "First name is required")
    @param:JsonProperty("first_name")
    val firstName: String,
    
    @field:NotBlank(message = "Last name is required")
    @param:JsonProperty("last_name")
    val lastName: String,
    
    @field:Email(message = "Invalid email format")
    @field:NotBlank(message = "Email is required")
    val email: String,
    
    @field:NotBlank(message = "Password is required")
    val password: String,
    
    @field:NotNull(message = "Role ID is required")
    @param:JsonProperty("role_id")
    val roleId: Int,
    
    @field:NotNull(message = "Department ID is required")
    @param:JsonProperty("dept_id")
    val deptId: Int,
    
    @param:JsonProperty("reporting_to")
    val reportingTo: UUID? = null
)
