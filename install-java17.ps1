
# Java 17 Installation Script for veterinariFx
# Download and install Eclipse Adoptium Temurin JDK 17

# Create directories
New-Item -Path 'C:\Program Files\Java' -ItemType Directory -Force

# Download JDK 17
Write-Host 'Downloading Eclipse Adoptium Temurin JDK 17...'
Invoke-WebRequest -Uri 'https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.13%2B11/OpenJDK17U-jdk_x64_windows_hotspot_17.0.13_11.zip' -OutFile 'C:\temp\jdk17.zip'

# Extract
Write-Host 'Extracting JDK...'
Expand-Archive -Path 'C:\temp\jdk17.zip' -DestinationPath 'C:\Program Files\Java'

# Rename extracted folder
Get-ChildItem -Path 'C:\Program Files\Java' -Directory | Where-Object Name -like '*jdk*' | Rename-Item -NewName 'jdk-17'

# Set JAVA_HOME
Set-ItemProperty -Path 'HKLM:\SYSTEM\CurrentControlSet\Control\Session Manager\Environment' -Name 'JAVA_HOME' -Value 'C:\Program Files\Java\jdk-17'

# Refresh environment
 = 'C:\Program Files\Java\jdk-17'
C:\ProgramData\Oracle\Java\javapath;C:\WINDOWS\system32;C:\WINDOWS;C:\WINDOWS\System32\Wbem;C:\WINDOWS\System32\WindowsPowerShell\v1.0\;C:\WINDOWS\System32\OpenSSH\;C:\Program Files\dotnet\;C:\Program Files (x86)\NVIDIA Corporation\PhysX\Common;C:\Program Files\NVIDIA Corporation\NVIDIA App\NvDLISR;C:\Users\Usuario\AppData\Local\Microsoft\WindowsApps;C:\Users\Usuario\AppData\Local\Programs\Antigravity IDE\bin = C:\ProgramData\Oracle\Java\javapath;C:\WINDOWS\system32;C:\WINDOWS;C:\WINDOWS\System32\Wbem;C:\WINDOWS\System32\WindowsPowerShell\v1.0\;C:\WINDOWS\System32\OpenSSH\;C:\Program Files\dotnet\;C:\Program Files (x86)\NVIDIA Corporation\PhysX\Common;C:\Program Files\NVIDIA Corporation\NVIDIA App\NvDLISR;C:\Users\Usuario\AppData\Local\Microsoft\WindowsApps;C:\Users\Usuario\AppData\Local\Programs\Antigravity IDE\bin + ';C:\Program Files\Java\jdk-17\bin'

Write-Host 'Java 17 installed successfully!'
Write-Host 'Please restart your command prompt or run: $env:JAVA_HOME = 'C:\Program Files\Java\jdk-17''

