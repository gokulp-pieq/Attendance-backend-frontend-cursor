package com.attendance.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class CreateEmployeeRequest(
    @field:NotBlank(message = "First name is required")
    @JsonProperty("first_name")
    val firstName: String,
    
    @field:NotBlank(message = "Last name is required")
    @JsonProperty("last_name")
    val lastName: String,
    
    @field:Email(message = "Invalid email format")
    @field:NotBlank(message = "Email is required")
    @JsonProperty("email")
    val email: String,
    
    @field:NotBlank(message = "Password is required")
    @JsonProperty("password")
    val password: String,
    
    @field:NotBlank(message = "Role name is required")
    @JsonProperty("role_name")
    val roleName: String,
    
    @field:NotBlank(message = "Department name is required")
    @JsonProperty("dept_name")
    val deptName: String,
    
    @JsonProperty("reporting_to")
    val reportingTo: String? = null
) {
    // Add a no-argument constructor for Jackson
    constructor() : this("", "", "", "", "", "", null)
}
