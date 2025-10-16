@echo off
echo Starting Chat Service...
echo.
echo Make sure you have Java 17+ and Maven installed
echo.
echo If Maven is not installed, you can:
echo 1. Download from https://maven.apache.org/download.cgi
echo 2. Or use Chocolatey: choco install maven
echo.
pause
echo.
echo Compiling project...
call mvn clean compile
if %errorlevel% neq 0 (
    echo.
    echo Compilation failed! Please check the errors above.
    pause
    exit /b 1
)
echo.
echo Starting application on http://localhost:8080
echo.
echo To test:
echo 1. Open test.html in your browser
echo 2. Or use Postman WebSocket: ws://localhost:8080/ws-chat
echo.
call mvn spring-boot:run
