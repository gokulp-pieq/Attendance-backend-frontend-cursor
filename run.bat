@echo off
echo Setting timezone to UTC...
set JAVA_OPTS=-Duser.timezone=UTC -Dfile.encoding=UTF-8 -Djava.locale.providers=COMPAT -Duser.country=US -Duser.language=en
echo Starting application with timezone: UTC
echo JAVA_OPTS: %JAVA_OPTS%
java %JAVA_OPTS% -jar build\libs\AttendanceCursor2-1.0.0-all.jar server config.yml
pause
