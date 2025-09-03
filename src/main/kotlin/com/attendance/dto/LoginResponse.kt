package com.attendance.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class LoginResponse(
    val id: Int,
    @JsonProperty("emp_id")
    val empId: UUID,
    @JsonProperty("first_name")
    val firstName: String,
    @JsonProperty("last_name")
    val lastName: String,
    val email: String,
    @JsonProperty("role_id")
    val roleId: Int,
    @JsonProperty("dept_id")
    val deptId: Int,
    @JsonProperty("reporting_to")
    val reportingTo: UUID?
)
