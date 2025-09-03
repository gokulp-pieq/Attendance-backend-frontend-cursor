package com.attendance

import com.attendance.config.AttendanceConfiguration
import com.attendance.db.DatabaseBundle
import com.attendance.db.DatabaseHealthCheck
import com.attendance.resources.EmployeeResource
import com.attendance.resources.AttendanceResource
import io.dropwizard.core.Application
import io.dropwizard.core.setup.Bootstrap
import io.dropwizard.core.setup.Environment
import io.dropwizard.jdbi3.JdbiFactory
import org.jdbi.v3.core.Jdbi

class AttendanceApplication : Application<AttendanceConfiguration>() {

    override fun getName(): String = "attendance-management"

    override fun initialize(bootstrap: Bootstrap<AttendanceConfiguration>) {
        bootstrap.addBundle(DatabaseBundle())
    }

    override fun run(configuration: AttendanceConfiguration, environment: Environment) {
        // Configure JDBI
        val factory = JdbiFactory()
        val jdbi: Jdbi = factory.build(environment, configuration.database, "database")
        
        // Register resources
        environment.jersey().register(EmployeeResource(jdbi))
        environment.jersey().register(AttendanceResource(jdbi))
        
        // Register health checks
        environment.healthChecks().register("database-connection", DatabaseHealthCheck(jdbi))
    }
}

fun main(args: Array<String>) {
    AttendanceApplication().run(*args)
}
