# Compiles the project and runs the AssignmentOne demonstration.

. "$PSScriptRoot\Find-Jdk.ps1"

$projectRoot = Split-Path $PSScriptRoot -Parent
$jdk = Find-JdkPath

if (-not $jdk) {
    Write-Host "No JDK found. Install a JDK (17 or later is recommended) and try again." -ForegroundColor Red
    exit 1
}

& (Join-Path $PSScriptRoot "build.ps1")
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

$java = Join-Path $jdk "bin\java.exe"
& $java -cp (Join-Path $projectRoot "out") AssignmentOne
exit $LASTEXITCODE
