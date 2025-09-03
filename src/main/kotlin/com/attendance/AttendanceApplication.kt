package com.attendance

import com.attendance.config.AttendanceConfiguration
import com.attendance.db.DatabaseBundle
import com.attendance.db.DatabaseHealthCheck
import com.attendance.resources.EmployeeResource
import com.attendance.resources.AttendanceResource
import com.attendance.resources.AuthResource
import io.dropwizard.core.Application
import io.dropwizard.core.setup.Bootstrap
import io.dropwizard.core.setup.Environment
import io.dropwizard.jdbi3.JdbiFactory
import org.jdbi.v3.core.Jdbi
import jakarta.servlet.FilterRegistration

class AttendanceApplication : Application<AttendanceConfiguration>() {

    override fun getName(): String = "attendance-management"

    override fun initialize(bootstrap: Bootstrap<AttendanceConfiguration>) {
        bootstrap.addBundle(DatabaseBundle())
    }

    override fun run(configuration: AttendanceConfiguration, environment: Environment) {
        // Configure CORS filter
        val cors: FilterRegistration.Dynamic = environment.servlets().addFilter("CORS", org.eclipse.jetty.servlets.CrossOriginFilter::class.java)
        cors.setInitParameter(org.eclipse.jetty.servlets.CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*")
        cors.setInitParameter(org.eclipse.jetty.servlets.CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin,Authorization")
        cors.setInitParameter(org.eclipse.jetty.servlets.CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD")
        cors.setInitParameter(org.eclipse.jetty.servlets.CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true")
        cors.addMappingForUrlPatterns(null, true, "/*")
        
        // Configure JDBI
        val factory = JdbiFactory()
        val jdbi: Jdbi = factory.build(environment, configuration.database, "database")
        
        // Register resources
        environment.jersey().register(EmployeeResource(jdbi))
        environment.jersey().register(AttendanceResource(jdbi))
        environment.jersey().register(AuthResource(jdbi))
        
        // Register health checks
        environment.healthChecks().register("database-connection", DatabaseHealthCheck(jdbi))
    }
}

fun main(args: Array<String>) {
    AttendanceApplication().run(*args)
}
