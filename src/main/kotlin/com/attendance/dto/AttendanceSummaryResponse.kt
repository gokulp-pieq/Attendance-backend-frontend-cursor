package com.attendance.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.time.Duration

data class AttendanceSummaryResponse(
    @JsonProperty("emp_id")
    val empId: String,
    
    @JsonProperty("employee_name")
    val employeeName: String,
    
    @JsonProperty("date")
    val date: String,
    
    @JsonProperty("checkin_time")
    val checkinTime: String,
    
    @JsonProperty("checkout_time")
    val checkoutTime: String?,
    
    @JsonProperty("total_hours")
    val totalHours: String?,
    
    @JsonProperty("status")
    val status: String
)
