package com.attendance.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class EmployeeResponse(
    val id: Int? = null,
    @JsonProperty("emp_id")
    val empId: UUID,
    @JsonProperty("first_name")
    val firstName: String,
    @JsonProperty("last_name")
    val lastName: String,
    val email: String,
    @JsonProperty("role_id")
    val roleId: Int,
    @JsonProperty("role_name")
    val roleName: String,
    @JsonProperty("dept_id")
    val deptId: Int,
    @JsonProperty("dept_name")
    val deptName: String,
    @JsonProperty("reporting_to")
    val reportingTo: UUID?
)
