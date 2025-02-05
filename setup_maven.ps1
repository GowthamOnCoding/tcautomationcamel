# Create directory for Maven
$mavenDir = "C:\Program Files\Maven"
if (-not (Test-Path $mavenDir)) {
    New-Item -ItemType Directory -Path $mavenDir
}

# Download Maven
$mavenVersion = "3.9.6"
$downloadUrl = "https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-$mavenVersion-bin.zip"
$zipFile = "$env:TEMP\maven.zip"
$mavenPath = "$mavenDir\apache-maven-$mavenVersion"

Write-Host "Downloading Maven $mavenVersion..."
Invoke-WebRequest -Uri $downloadUrl -OutFile $zipFile

Write-Host "Extracting Maven..."
Expand-Archive -Path $zipFile -DestinationPath $mavenDir -Force
Remove-Item $zipFile

# Set environment variables
Write-Host "Setting environment variables..."
[System.Environment]::SetEnvironmentVariable("MAVEN_HOME", $mavenPath, [System.EnvironmentVariableTarget]::Machine)

# Add to PATH if not already present
$path = [System.Environment]::GetEnvironmentVariable("Path", [System.EnvironmentVariableTarget]::Machine)
if (-not $path.Contains("%MAVEN_HOME%\bin")) {
    [System.Environment]::SetEnvironmentVariable("Path", "$path;%MAVEN_HOME%\bin", [System.EnvironmentVariableTarget]::Machine)
}

Write-Host "Maven installation complete. Please restart your terminal/IDE for changes to take effect."
Write-Host "You can verify the installation by running: mvn -version"
