package com.attendance.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.util.UUID
import jakarta.validation.constraints.NotNull

data class Attendance(
    val id: Int? = null,
    @field:NotNull(message = "Employee ID is required")
    val empId: UUID,
    @field:NotNull(message = "Check-in datetime is required")
    val checkinDatetime: LocalDateTime,
    val checkoutDatetime: LocalDateTime? = null
) {
    @JsonProperty("emp_id")
    fun getEmpId(): String = empId.toString()
    
    @JsonProperty("checkin_datetime")
    fun getCheckinDatetime(): String = checkinDatetime.toString()
    
    @JsonProperty("checkout_datetime")
    fun getCheckoutDatetime(): String? = checkoutDatetime?.toString()
}
