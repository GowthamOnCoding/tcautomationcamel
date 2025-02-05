# Download Maven MSI installer
$downloadUrl = "https://downloads.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip"
$tempDir = [System.IO.Path]::GetTempPath()
$zipPath = Join-Path $tempDir "maven.zip"
$mavenDir = "C:\apache-maven-3.9.6"

Write-Host "Downloading Maven..."
Invoke-WebRequest -Uri $downloadUrl -OutFile $zipPath

Write-Host "Extracting Maven..."
Expand-Archive -Path $zipPath -DestinationPath "C:\" -Force
Remove-Item $zipPath

# Set environment variables
Write-Host "Setting environment variables..."
[System.Environment]::SetEnvironmentVariable("M2_HOME", $mavenDir, [System.EnvironmentVariableTarget]::Machine)
[System.Environment]::SetEnvironmentVariable("MAVEN_HOME", $mavenDir, [System.EnvironmentVariableTarget]::Machine)

# Update PATH
$path = [System.Environment]::GetEnvironmentVariable("Path", [System.EnvironmentVariableTarget]::Machine)
$mavenBinPath = "$mavenDir\bin"
if (-not $path.Contains($mavenBinPath)) {
    [System.Environment]::SetEnvironmentVariable("Path", "$path;$mavenBinPath", [System.EnvironmentVariableTarget]::Machine)
}

# Set JAVA_HOME if not already set
$javaHome = [System.Environment]::GetEnvironmentVariable("JAVA_HOME", [System.EnvironmentVariableTarget]::Machine)
if (-not $javaHome) {
    $javaPath = (Get-Command java).Path
    $javaHome = (Get-Item $javaPath).Directory.Parent.Parent.FullName
    [System.Environment]::SetEnvironmentVariable("JAVA_HOME", $javaHome, [System.EnvironmentVariableTarget]::Machine)
    Write-Host "Set JAVA_HOME to: $javaHome"
}

Write-Host @"
Maven installation complete!

To use Maven:
1. Close and reopen your terminal/IDE
2. Run 'mvn -version' to verify the installation

Current environment variables:
- MAVEN_HOME: $([System.Environment]::GetEnvironmentVariable("MAVEN_HOME", [System.EnvironmentVariableTarget]::Machine))
- M2_HOME: $([System.Environment]::GetEnvironmentVariable("M2_HOME", [System.EnvironmentVariableTarget]::Machine))
- JAVA_HOME: $([System.Environment]::GetEnvironmentVariable("JAVA_HOME", [System.EnvironmentVariableTarget]::Machine))

"@
