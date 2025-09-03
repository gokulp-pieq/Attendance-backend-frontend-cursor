package com.attendance.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.util.UUID
import jakarta.validation.constraints.NotNull

data class CheckinRequest @JsonCreator constructor(
    @field:NotNull(message = "Employee ID is required")
    @param:JsonProperty("emp_id")
    val empId: UUID,
    
    @param:JsonProperty("checkin_datetime")
    val checkinDatetime: LocalDateTime = LocalDateTime.now()
)
