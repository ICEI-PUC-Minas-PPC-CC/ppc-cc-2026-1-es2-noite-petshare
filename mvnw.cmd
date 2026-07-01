@echo off
setlocal
set DIRNAME=%~dp0
java -jar "%DIRNAME%\.mvn\wrapper\maven-wrapper.jar" %*
endlocal
