package com.attendance.db

import com.attendance.config.AttendanceConfiguration
import io.dropwizard.migrations.MigrationsBundle
import io.dropwizard.db.PooledDataSourceFactory

class DatabaseBundle : MigrationsBundle<AttendanceConfiguration>() {
    override fun getDataSourceFactory(configuration: AttendanceConfiguration): PooledDataSourceFactory {
        return configuration.database
    }
}
