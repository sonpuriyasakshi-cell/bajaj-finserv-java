@echo off
@setlocal EnableExtensions EnableDelayedExpansion

set "DIRNAME=%~dp0"
if "%DIRNAME%" == "" set "DIRNAME=.\"

set "MAVEN_PROJECTBASEDIR=%DIRNAME%"
if "%MAVEN_PROJECTBASEDIR:~-1%" == "\" set "MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR:~0,-1%"

set "WRAPPER_JAR=%DIRNAME%.mvn\wrapper\maven-wrapper.jar"
set "WRAPPER_PROPERTIES=%DIRNAME%.mvn\wrapper\maven-wrapper.properties"

if not "%JAVA_HOME%" == "" (
  set "JAVACMD=%JAVA_HOME%\bin\java.exe"
) else (
  set "JAVACMD=java.exe"
)

if not exist "%WRAPPER_JAR%" (
  echo Downloading Maven Wrapper jar...
  powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object System.Net.WebClient).DownloadFile('https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar', '%WRAPPER_JAR%')"
)

if "!MAVEN_OPTS!" == "" (
  "%JAVACMD%" -classpath "%WRAPPER_JAR%" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" org.apache.maven.wrapper.MavenWrapperMain %*
) else (
  "%JAVACMD%" !MAVEN_OPTS! -classpath "%WRAPPER_JAR%" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" org.apache.maven.wrapper.MavenWrapperMain %*
)

if ERRORLEVEL 1 goto error
goto end

:error
exit /B %ERRORLEVEL%

:end
@endlocal
