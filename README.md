# Flavor 🍽️

A modern Android recipe and food discovery app built with Clean Architecture and Jetpack Compose.

[![GitHub](https://img.shields.io/badge/GitHub-Nikunja--Sharma%2FFlavor-blue?logo=github)](https://github.com/Nikunja-Sharma/Flavor)

## Features

- 🔐 Google Sign-In authentication via Firebase
- 🍳 Discover and browse recipes
- 📱 Modern Material 3 UI with dynamic theming
- 🏗️ Clean Architecture with clear separation of concerns
- 💉 Dependency injection with Hilt
- 🗄️ Local persistence with Room database
- 🌐 Network layer with Retrofit
- 🔄 Reactive data flow with Kotlin Coroutines & StateFlow

## Tech Stack

| Category | Technology |
|----------|------------|
| Language | Kotlin 2.0 |
| UI | Jetpack Compose + Material 3 |
| Architecture | Clean Architecture + MVVM |
| DI | Hilt |
| Database | Room |
| Networking | Retrofit + OkHttp |
| Auth | Firebase Authentication |
| Async | Coroutines + StateFlow |
| Image Loading | Coil |
| Navigation | Compose Navigation |

## Project Structure

```
com.nikunja.testapp
├── data/                    # Data layer
│   ├── local/               # Room database
│   │   ├── dao/             # Data Access Objects
│   │   ├── database/        # Database configuration
│   │   └── entity/          # Database entities
│   ├── remote/              # Network layer
│   │   ├── api/             # Retrofit API interfaces
│   │   ├── dto/             # Data Transfer Objects
│   │   └── interceptor/     # OkHttp interceptors
│   └── repository/          # Repository implementations
├── domain/                  # Domain layer
│   ├── model/               # Business models
│   ├── repository/          # Repository interfaces
│   ├── usecase/             # Business logic use cases
│   └── util/                # Domain utilities
├── ui/                      # Presentation layer
│   ├── navigation/          # Compose Navigation
│   ├── screens/             # Feature screens
│   │   ├── home/            # Home feature
│   │   ├── login/           # Login feature
│   │   └── profile/         # Profile feature
│   └── theme/               # Material theme
├── di/                      # Hilt modules
├── MainActivity.kt          # Entry point
└── TestApp.kt               # Application class
```

## Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- JDK 17
- Android SDK 35

### Firebase Setup

1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com)
2. Enable Google Sign-In in Authentication
3. Download `google-services.json` and place in `app/`
4. Add your SHA-1 fingerprint to Firebase project settings

### Build & Run

```bash
# Clone the repository
git clone https://github.com/Nikunja-Sharma/Flavor.git

# Open in Android Studio and sync Gradle

# Run on device/emulator
./gradlew installDebug
```

## Architecture Overview

This app follows Clean Architecture principles with three distinct layers:

### Data Layer
Handles data operations from network and local database. Contains repository implementations, DTOs, and entities.

### Domain Layer
Contains business logic and is independent of any framework. Defines repository interfaces, use cases, and domain models.

### UI Layer
Presentation layer using Jetpack Compose with MVVM pattern. ViewModels expose state via StateFlow, and Composables are stateless.

## Screens

| Screen | Description |
|--------|-------------|
| Login | Google Sign-In authentication |
| Home | Recipe list with pull-to-refresh |
| Profile | User info and sign-out |

## Configuration

### API Base URL
Update the base URL in `AppModule.kt`:
```kotlin
.baseUrl("https://your-api.com/")
```

### Web Client ID
Set your Google OAuth client ID in `strings.xml`:
```xml
<string name="default_web_client_id">your-client-id</string>
```

## Author

**Nikunja Sharma**
- GitHub: [@Nikunja-Sharma](https://github.com/Nikunja-Sharma)

## License

This project is for demonstration purposes.
