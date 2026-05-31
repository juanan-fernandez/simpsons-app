# BigSchoolExample — AGENTS.md

Single-module Android app. Kotlin + Jetpack Compose (Material 3) + Gradle 9.4.1.

## Build & Test

All commands via `./gradlew` from repo root.

| Action | Command |
|---|---|
| Build debug | `assembleDebug` |
| Run unit tests | `testDebugUnitTest` |
| Run instrumented tests | `connectedDebugAndroidTest` (needs device/emulator) |
| All verifications | `check` (lint + all tests) |
| Clean | `clean` |

## Key Facts

- **Single module** `:app`, no monorepo. Root project name: `BigSchoolExample`.
- **Version catalog** at `gradle/libs.versions.toml` — add dependencies there before referencing in `build.gradle.kts`.
- **Kotlin 2.2.10 + AGP 9.2.1**. Compose compiler uses `org.jetbrains.kotlin.plugin.compose` plugin (not `composeOptions`).
- **JDK 21** required (toolchain, foojay-resolver), source/target compatibility is Java 11.
- **minSdk 26, compileSdk 36, targetSdk 36**.
- **No CI configuration** exists.
- **No instruction files existed** before this one.

## App Architecture

- Entrypoint: `app/src/main/java/com/example/bigschoolexample/MainActivity.kt`
- Theme: `app/src/main/java/com/example/bigschoolexample/ui/theme/`
- Release builds have `isMinifyEnabled = false`.
- Uses `enableEdgeToEdge()` (edge-to-edge display).
- Material 3 dynamic color on Android 12+, fallback purple/pink palette.

## Testing

- **Unit tests** (`src/test/`): JUnit 4, run with `testDebugUnitTest`.
- **Instrumented tests** (`src/androidTest/`): Compose UI Test + Espresso, run with `connectedDebugAndroidTest`.
- AndroidJUnitRunner configured as the test instrumentation runner.
