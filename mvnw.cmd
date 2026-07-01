@echo off
setlocal enabledelayedexpansion
set "DIRNAME=%~dp0"
rem ensure trailing backslash
if not "%DIRNAME:~-1%"=="\" set "DIRNAME=%DIRNAME%\"
set "WRAPPER_JAR=%DIRNAME%\.mvn\wrapper\maven-wrapper.jar"
echo Downloading maven-wrapper.jar (forcing overwrite)...
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/io/takari/maven-wrapper/0.5.6/maven-wrapper-0.5.6.jar' -OutFile '%WRAPPER_JAR%' -UseBasicParsing"
if not exist "%WRAPPER_JAR%" (
	echo Failed to download maven-wrapper.jar >&2
	exit /b 1
)
java -jar "%WRAPPER_JAR%" %*
endlocal
