package com.attendance.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class LoginRequest @JsonCreator constructor(
    @field:Email(message = "Invalid email format")
    @field:NotBlank(message = "Email is required")
    @param:JsonProperty("email")
    val email: String,

    @field:NotBlank(message = "Password is required")
    @param:JsonProperty("password")
    val password: String
)
