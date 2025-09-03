package com.attendance.db

import com.codahale.metrics.health.HealthCheck
import org.jdbi.v3.core.Jdbi

class DatabaseHealthCheck(private val jdbi: Jdbi) : HealthCheck() {
    
    override fun check(): Result {
        return try {
            jdbi.withHandle<Boolean, Exception> { handle ->
                handle.execute("SELECT 1")
                true
            }
            Result.healthy()
        } catch (e: Exception) {
            Result.unhealthy("Database connection failed", e)
        }
    }
}
