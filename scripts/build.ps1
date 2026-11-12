# Compiles every class in src into out.
# Run from anywhere: the script locates the project folder from its own path.

. "$PSScriptRoot\Find-Jdk.ps1"

$projectRoot = Split-Path $PSScriptRoot -Parent
$jdk = Find-JdkPath

if (-not $jdk) {
    Write-Host "No JDK found. Install a JDK (17 or later is recommended) and try again." -ForegroundColor Red
    Write-Host "The Java extension for VS Code also needs a JDK to provide code completion."
    exit 1
}

$javac = Join-Path $jdk "bin\javac.exe"
$out = Join-Path $projectRoot "out"
New-Item -ItemType Directory -Force -Path $out | Out-Null

$sources = Get-ChildItem (Join-Path $projectRoot "src") -Filter *.java | ForEach-Object { $_.FullName }

Write-Host "Using $jdk"
Write-Host "Compiling $($sources.Count) source files to out\"

& $javac -encoding UTF-8 -d $out $sources
if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation failed." -ForegroundColor Red
    exit $LASTEXITCODE
}
Write-Host "Compilation succeeded." -ForegroundColor Green
