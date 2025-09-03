package com.attendance.config

import io.dropwizard.core.Configuration
import io.dropwizard.db.DataSourceFactory
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

class AttendanceConfiguration : Configuration() {
    
    @Valid
    @NotNull
    var database: DataSourceFactory = DataSourceFactory()
    
    fun getDataSourceFactory(): DataSourceFactory = database
}
