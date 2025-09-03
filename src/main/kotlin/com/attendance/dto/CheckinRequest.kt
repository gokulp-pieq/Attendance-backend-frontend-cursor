package com.attendance.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.util.UUID
import jakarta.validation.constraints.NotNull

data class CheckinRequest(
    @field:NotNull(message = "Employee ID is required")
    @JsonProperty("emp_id")
    val empId: UUID,
    
    @JsonProperty("checkin_datetime")
    val checkinDatetime: LocalDateTime = LocalDateTime.now()
)
