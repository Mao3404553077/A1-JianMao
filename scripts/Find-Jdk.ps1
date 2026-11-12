# Finds a JDK on this machine and prints the JDK home.
#
# Order: JAVA_HOME, then javac on the PATH, then the usual vendor install
# folders, and finally any application that ships its own private JDK (some
# design tools do). This script is a development convenience only - it is not
# part of the assessed program.

function Find-JdkPath {
    $candidates = @()

    if ($env:JAVA_HOME) {
        $candidates += $env:JAVA_HOME
    }

    $onPath = Get-Command javac -ErrorAction SilentlyContinue
    if ($onPath) {
        $candidates += (Split-Path (Split-Path $onPath.Source -Parent) -Parent)
    }

    $vendorRoots = @(
        "$env:ProgramFiles\Eclipse Adoptium",
        "$env:ProgramFiles\Java",
        "$env:ProgramFiles\Microsoft",
        "$env:ProgramFiles\Amazon Corretto",
        "$env:ProgramFiles\Zulu",
        "$env:ProgramFiles\BellSoft",
        "$env:LOCALAPPDATA\Programs\Eclipse Adoptium",
        "$env:USERPROFILE\.jdks"
    )
    foreach ($root in $vendorRoots) {
        if (Test-Path $root) {
            Get-ChildItem $root -Directory -ErrorAction SilentlyContinue |
                ForEach-Object { $candidates += $_.FullName }
        }
    }

    # A JDK unpacked into the user's home folder, for example C:\Users\me\jdk-21.0.2.
    if (Test-Path $env:USERPROFILE) {
        Get-ChildItem $env:USERPROFILE -Directory -ErrorAction SilentlyContinue |
            Where-Object { $_.Name -like "jdk*" } |
            ForEach-Object { $candidates += $_.FullName }
    }

    # Applications that bundle a private JDK, for example ...\<App>\jre.
    if (Test-Path $env:ProgramFiles) {
        Get-ChildItem $env:ProgramFiles -Directory -ErrorAction SilentlyContinue |
            ForEach-Object { $candidates += (Join-Path $_.FullName "jre") }
    }

    foreach ($candidate in $candidates) {
        if ($candidate -and (Test-Path (Join-Path $candidate "bin\javac.exe"))) {
            return (Resolve-Path $candidate).Path
        }
    }
    return $null
}
