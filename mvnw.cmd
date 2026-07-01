@echo off
setlocal enabledelayedexpansion
set "DIRNAME=%~dp0"
rem ensure trailing backslash
if not "%DIRNAME:~-1%"=="\" set "DIRNAME=%DIRNAME%\"
set "WRAPPER_JAR=%DIRNAME%\.mvn\wrapper\maven-wrapper.jar"
if not exist "%WRAPPER_JAR%" (
	echo Downloading maven-wrapper.jar...
	powershell -Command "(New-Object System.Net.WebClient).DownloadFile('https://repo1.maven.org/maven2/io/takari/maven-wrapper/0.5.6/maven-wrapper-0.5.6.jar','%WRAPPER_JAR%')"
)
java -jar "%WRAPPER_JAR%" %*
endlocal
