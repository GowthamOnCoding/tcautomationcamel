# Function to find Java installation
function Find-JavaHome {
    $javaExecutable = Get-Command java -ErrorAction SilentlyContinue
    if ($javaExecutable) {
        $javaPath = (Get-Item $javaExecutable.Source).Directory.Parent.FullName
        return $javaPath
    }
    
    # Check common installation directories
    $commonPaths = @(
        "C:\Program Files\Java\*",
        "C:\Program Files (x86)\Java\*",
        "${env:ProgramFiles}\Java\*",
        "${env:ProgramFiles(x86)}\Java\*"
    )
    
    foreach ($path in $commonPaths) {
        $jdkPaths = Get-ChildItem -Path $path -ErrorAction SilentlyContinue | 
                    Where-Object { $_.PSIsContainer -and $_.Name -like "jdk*" } |
                    Sort-Object LastWriteTime -Descending
        
        if ($jdkPaths -and $jdkPaths.Count -gt 0) {
            return $jdkPaths[0].FullName
        }
    }
    
    return $null
}

# Find Java installation
$javaHome = Find-JavaHome
if (-not $javaHome) {
    Write-Host "Could not find Java installation. Please install JDK and try again."
    exit 1
}

# Set JAVA_HOME
Write-Host "Setting JAVA_HOME to: $javaHome"
[System.Environment]::SetEnvironmentVariable("JAVA_HOME", $javaHome, [System.EnvironmentVariableTarget]::Machine)

# Update Path if needed
$path = [System.Environment]::GetEnvironmentVariable("Path", [System.EnvironmentVariableTarget]::Machine)
$javaBinPath = Join-Path $javaHome "bin"
if (-not $path.Contains($javaBinPath)) {
    [System.Environment]::SetEnvironmentVariable("Path", "$path;$javaBinPath", [System.EnvironmentVariableTarget]::Machine)
}

Write-Host @"
Java environment variables have been set:
JAVA_HOME: $javaHome
Please close and reopen your terminal/IDE for changes to take effect.

To verify:
1. Open a new terminal
2. Run: echo %JAVA_HOME%
3. Run: java -version
"@
