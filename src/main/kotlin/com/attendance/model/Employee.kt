package com.attendance.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class Employee(
    val id: Int? = null,
    @field:JsonProperty("emp_id")
    val empId: UUID = UUID.randomUUID(),
    @field:JsonProperty("first_name")
    @field:NotBlank(message = "First name is required")
    val firstName: String,
    @field:JsonProperty("last_name")
    @field:NotBlank(message = "Last name is required")
    val lastName: String,
    @field:JsonProperty("email")
    @field:Email(message = "Invalid email format")
    @field:NotBlank(message = "Email is required")
    val email: String,
    @field:JsonProperty("role_id")
    @field:NotNull(message = "Role ID is required")
    val roleId: Int,
    @field:JsonProperty("dept_id")
    @field:NotNull(message = "Department ID is required")
    val deptId: Int,
    @field:JsonProperty("reporting_to")
    val reportingTo: UUID? = null
)
