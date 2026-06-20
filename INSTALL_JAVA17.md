# Java 17 Installation Required

This project requires Java 17 to build and run.

## Installation Instructions

### Option 1: Manual Installation

1. Download Eclipse Adoptium Temurin JDK 17:
   - Visit: https://adoptium.net/temurin/releases/
   - Download the latest JDK 17 release for Windows
   - Extract to `C:\Program Files\Java\jdk-17`

2. Set JAVA_HOME environment variable:
   - Right-click "This PC" → Properties → Advanced system settings
   - Click "Environment Variables"
   - Add or edit `JAVA_HOME` to point to `C:\Program Files\Java\jdk-17`
   - Update `Path` to include `C:\Program Files\Java\jdk-17\bin`

3. Restart your command prompt or IDE

### Option 2: Using SDKMAN

If you have SDKMAN installed:
```bash
sdk install java 17.0.13-tem
sdk use java 17.0.13-tem
```

### Option 3: Using Chocolatey

```bashnchoco install openjdk17
```

## Verification

After installation, verify Java 17 is available:
```bash
java -version
# Should output: openjdk version "17.0.13" 2024-10-21
```

## Build and Run

Once Java 17 is installed:
```bashn./mvnw compile
./mvnw javafx:run
```

## Note

The system currently has Java 8.0.51 installed, which is not compatible with this project.
