@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  AttendanceCursor2 startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
@rem This is normally unused
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and ATTENDANCE_CURSOR2_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\AttendanceCursor2-1.0.0.jar;%APP_HOME%\lib\dropwizard-jdbi3-4.0.4.jar;%APP_HOME%\lib\dropwizard-migrations-4.0.4.jar;%APP_HOME%\lib\dropwizard-core-4.0.4.jar;%APP_HOME%\lib\dropwizard-db-4.0.4.jar;%APP_HOME%\lib\dropwizard-configuration-4.0.4.jar;%APP_HOME%\lib\dropwizard-health-4.0.4.jar;%APP_HOME%\lib\dropwizard-jersey-4.0.4.jar;%APP_HOME%\lib\dropwizard-jetty-4.0.4.jar;%APP_HOME%\lib\dropwizard-request-logging-4.0.4.jar;%APP_HOME%\lib\dropwizard-logging-4.0.4.jar;%APP_HOME%\lib\dropwizard-metrics-4.0.4.jar;%APP_HOME%\lib\dropwizard-validation-4.0.4.jar;%APP_HOME%\lib\jetty-servlets-11.0.18.jar;%APP_HOME%\lib\postgresql-42.7.1.jar;%APP_HOME%\lib\flyway-core-10.7.1.jar;%APP_HOME%\lib\dropwizard-jackson-4.0.4.jar;%APP_HOME%\lib\metrics-jakarta-servlets-4.2.22.jar;%APP_HOME%\lib\metrics-json-4.2.22.jar;%APP_HOME%\lib\jackson-dataformat-toml-2.16.1.jar;%APP_HOME%\lib\jackson-dataformat-yaml-2.16.1.jar;%APP_HOME%\lib\jackson-datatype-guava-2.16.1.jar;%APP_HOME%\lib\jackson-datatype-jdk8-2.16.1.jar;%APP_HOME%\lib\jackson-datatype-jsr310-2.16.1.jar;%APP_HOME%\lib\jackson-module-blackbird-2.16.1.jar;%APP_HOME%\lib\jackson-module-parameter-names-2.16.1.jar;%APP_HOME%\lib\jackson-jakarta-rs-json-provider-2.16.1.jar;%APP_HOME%\lib\jackson-jakarta-rs-base-2.16.1.jar;%APP_HOME%\lib\jackson-module-jakarta-xmlbind-annotations-2.16.1.jar;%APP_HOME%\lib\jackson-databind-2.16.1.jar;%APP_HOME%\lib\dropwizard-servlets-4.0.4.jar;%APP_HOME%\lib\dropwizard-lifecycle-4.0.4.jar;%APP_HOME%\lib\dropwizard-util-4.0.4.jar;%APP_HOME%\lib\jackson-annotations-2.16.1.jar;%APP_HOME%\lib\jackson-core-2.16.1.jar;%APP_HOME%\lib\jackson-module-kotlin-2.16.1.jar;%APP_HOME%\lib\kotlin-reflect-1.9.22.jar;%APP_HOME%\lib\kotlin-stdlib-jdk7-1.9.22.jar;%APP_HOME%\lib\kotlin-stdlib-1.9.22.jar;%APP_HOME%\lib\kotlin-stdlib-jdk8-1.9.22.jar;%APP_HOME%\lib\jersey-bean-validation-3.0.12.jar;%APP_HOME%\lib\hibernate-validator-8.0.1.Final.jar;%APP_HOME%\lib\metrics-jersey3-4.2.22.jar;%APP_HOME%\lib\jersey-container-servlet-3.0.12.jar;%APP_HOME%\lib\jersey-container-servlet-core-3.0.12.jar;%APP_HOME%\lib\jersey-server-3.0.12.jar;%APP_HOME%\lib\jakarta.validation-api-3.0.2.jar;%APP_HOME%\lib\jersey-metainf-services-3.0.12.jar;%APP_HOME%\lib\jersey-hk2-3.0.12.jar;%APP_HOME%\lib\jersey-client-3.0.12.jar;%APP_HOME%\lib\jersey-common-3.0.12.jar;%APP_HOME%\lib\jakarta.ws.rs-api-3.1.0.jar;%APP_HOME%\lib\metrics-jetty11-4.2.22.jar;%APP_HOME%\lib\metrics-jvm-4.2.22.jar;%APP_HOME%\lib\metrics-jmx-4.2.22.jar;%APP_HOME%\lib\metrics-healthchecks-4.2.22.jar;%APP_HOME%\lib\metrics-jdbi3-4.2.22.jar;%APP_HOME%\lib\metrics-logback-4.2.22.jar;%APP_HOME%\lib\metrics-core-4.2.22.jar;%APP_HOME%\lib\metrics-annotation-4.2.22.jar;%APP_HOME%\lib\logback-classic-1.4.11.jar;%APP_HOME%\lib\jdbi3-guava-3.41.3.jar;%APP_HOME%\lib\guava-32.1.3-jre.jar;%APP_HOME%\lib\caffeine-3.1.8.jar;%APP_HOME%\lib\checker-qual-3.41.0.jar;%APP_HOME%\lib\jakarta.servlet-api-5.0.0.jar;%APP_HOME%\lib\argparse4j-0.9.0.jar;%APP_HOME%\lib\jetty-servlet-11.0.18.jar;%APP_HOME%\lib\jetty-security-11.0.18.jar;%APP_HOME%\lib\jetty-server-11.0.18.jar;%APP_HOME%\lib\jetty-http-11.0.18.jar;%APP_HOME%\lib\jetty-io-11.0.18.jar;%APP_HOME%\lib\jetty-util-11.0.18.jar;%APP_HOME%\lib\jetty-setuid-java-1.0.4.jar;%APP_HOME%\lib\jakarta.inject-api-2.0.1.jar;%APP_HOME%\lib\jdbi3-sqlobject-3.41.3.jar;%APP_HOME%\lib\jdbi3-core-3.41.3.jar;%APP_HOME%\lib\jul-to-slf4j-2.0.9.jar;%APP_HOME%\lib\log4j-over-slf4j-2.0.9.jar;%APP_HOME%\lib\jcl-over-slf4j-2.0.9.jar;%APP_HOME%\lib\slf4j-api-2.0.9.jar;%APP_HOME%\lib\liquibase-core-4.24.0.jar;%APP_HOME%\lib\liquibase-slf4j-5.0.0.jar;%APP_HOME%\lib\jakarta.xml.bind-api-3.0.1.jar;%APP_HOME%\lib\classmate-1.6.0.jar;%APP_HOME%\lib\jakarta.el-4.0.2.jar;%APP_HOME%\lib\gson-2.10.1.jar;%APP_HOME%\lib\jboss-logging-3.5.3.Final.jar;%APP_HOME%\lib\opencsv-5.8.jar;%APP_HOME%\lib\commons-text-1.11.0.jar;%APP_HOME%\lib\logback-access-1.4.11.jar;%APP_HOME%\lib\logback-core-1.4.11.jar;%APP_HOME%\lib\error_prone_annotations-2.23.0.jar;%APP_HOME%\lib\logback-throttling-appender-1.4.1.jar;%APP_HOME%\lib\javassist-3.29.2-GA.jar;%APP_HOME%\lib\hk2-locator-3.0.3.jar;%APP_HOME%\lib\hk2-api-3.0.5.jar;%APP_HOME%\lib\hk2-utils-3.0.5.jar;%APP_HOME%\lib\jakarta.annotation-api-2.1.1.jar;%APP_HOME%\lib\profiler-1.1.1.jar;%APP_HOME%\lib\jetty-jakarta-servlet-api-5.0.2.jar;%APP_HOME%\lib\osgi-resource-locator-1.0.3.jar;%APP_HOME%\lib\tomcat-jdbc-10.1.15.jar;%APP_HOME%\lib\geantyref-1.3.14.jar;%APP_HOME%\lib\commons-lang3-3.13.0.jar;%APP_HOME%\lib\commons-collections4-4.4.jar;%APP_HOME%\lib\snakeyaml-2.2.jar;%APP_HOME%\lib\jakarta.activation-2.0.1.jar;%APP_HOME%\lib\failureaccess-1.0.1.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\jakarta.el-api-4.0.0.jar;%APP_HOME%\lib\annotations-13.0.jar;%APP_HOME%\lib\aopalliance-repackaged-3.0.5.jar;%APP_HOME%\lib\tomcat-juli-10.1.15.jar;%APP_HOME%\lib\jakarta.activation-api-2.1.0.jar


@rem Execute AttendanceCursor2
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %ATTENDANCE_CURSOR2_OPTS%  -classpath "%CLASSPATH%" com.attendance.AttendanceApplicationKt %*

:end
@rem End local scope for the variables with windows NT shell
if %ERRORLEVEL% equ 0 goto mainEnd

:fail
rem Set variable ATTENDANCE_CURSOR2_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%ATTENDANCE_CURSOR2_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
