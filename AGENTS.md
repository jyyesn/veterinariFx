# AGENTS.md — veterinariFx

## Project

JavaFX 17 veterinary clinic management app. MVC + custom data structures (no frameworks beyond JavaFX). All persistence via CSV files in `data/`.

## Build & Run

```bash
# Set JAVA_HOME to the environment's JDK 21 (fully compatible with Java 17 target)
$env:JAVA_HOME = "C:\Users\Usuario\.antigravity-ide\extensions\redhat.java-1.54.0-win32-x64\jre\21.0.10-win32-x86_64"

# Run the app (requires display)
./mvnw javafx:run

# Compile only
./mvnw compile
```

Java 17/21 required. The environment has a usable OpenJDK 21 at `C:\Users\Usuario\.antigravity-ide\extensions\redhat.java-1.54.0-win32-x64\jre\21.0.10-win32-x86_64`. Maven wrapper included (`.mvn/`).
Source directory is set directly to `src` (configured via `<sourceDirectory>` in `pom.xml`).

## Architecture

```
com.veterinaria/
  vista/          → JavaFX UI (MainApp is entrypoint)
  controlador/    → Controllers (thin, delegate to service)
  service/        → VeterinariaService (business logic, validation)
  dao/            → DAO interfaces + CSV implementations (in-memory cache via MapaAsociativo)
  model/          → Propietario, Mascota, Cita (POJOs with CSV serialization)
  estructuras/    → Custom data structures (ListaEnlazada, MapaAsociativo, ArbolBinarioBusqueda)
```

- Entry: `com.veterinaria.vista.MainApp` (defined in `pom.xml` javafx-maven-plugin)
- `module-info.java` at `src/` — required for JavaFX reflection; update it if adding packages
- All DAOs default to `data/*.csv` paths; CSV format: comma-separated fields, one record per line
- `VeterinariaService` has a DI constructor for testing: `VeterinariaService(PropietarioDAO, MascotaDAO, CitaDAO)`

## Key gotchas

- **Cascade deletes**: Deleting a Propietario cascades to Mascotas → Citas (handled in `VeterinariaService`, not DAO)
- **Cache-first writes**: DAOs keep a `MapaAsociativo` cache; every mutation rewrites the entire CSV file
- **No test framework configured**: No test dependencies or test sources exist yet
- **JavaFX thread**: UI must stay on JavaFX Application Thread; `VeterinariaService` runs synchronously (blocks UI on I/O)
- **No FXML**: All UI is built in Java code (no `.fxml` files despite `javafx-fxml` dependency)
