package com.attendance.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.util.UUID
import jakarta.validation.constraints.NotNull

data class CheckoutRequest @JsonCreator constructor(
    @field:NotNull(message = "Employee ID is required")
    @param:JsonProperty("emp_id")
    val empId: UUID,
    
    @param:JsonProperty("checkout_datetime")
    val checkoutDatetime: LocalDateTime = LocalDateTime.now()
)
