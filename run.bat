@echo off
REM Compile and run the A1 health appointment system.
REM Windows cmd does not expand "src\*.java", so the actual build and run are
REM done by PowerShell scripts that also locate a JDK on this machine.

powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0scripts\run.ps1"
echo.
pause
