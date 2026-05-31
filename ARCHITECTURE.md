# Architecture Guidelines - Android Kotlin Project

This document is the single source of truth for the architecture, patterns, and coding standards of this project. Any AI agent or developer MUST read and adhere to these rules before making any changes or adding new features.

---

## 1. Architectural Overview
This project uses **Clean Architecture** combined with the **MVVM (Model-View-ViewModel)** pattern.
* **No Multi-module Over-engineering:** All code lives in a single app module, separated strictly by a logical package structure.
* **Unidirectional Data Flow (UDF):** Data flows up from the data sources to the UI via Kotlin Flows/StateFlow. Actions flow down from the UI to the ViewModels and Use Cases.

---

## 2. Package & Layer Structure
The project is organized by layers to enforce strict separation of concerns. Do not bypass layers.

com.example.app/
│
├── data/                  # Data Layer (Retrofit, DataSources, Mappers, Repositories Impl)
│   ├── remote/            # API interfaces, DTOs, Network interception
│   ├── repository/        # Implementations of Domain Repositories
│   └── mapper/            # Functions to transform DTOs to Domain Models
│
├── domain/                # Domain Layer (Business Logic - Pure Kotlin, NO Android dependencies)
│   ├── model/             # Pure Kotlin data classes (Domain Entities)
│   ├── repository/        # Repository Interfaces
│   └── usecase/           # Single-responsibility business logic classes
│
├── presentation/          # Presentation Layer (UI & ViewModels)
│   ├── components/        # Reusable Compose widgets
│   ├── screen1/           # Feature-specific package
│   │   ├── Screen1Screen.kt      # Compose UI
│   │   └── Screen1ViewModel.kt   # Architecture ViewModel
│   └── theme/             # Design System (Color, Type, Theme)


---

## 3. Layer Rules & Communication

### 3.1 Domain Layer (The Core)
* Must be **pure Kotlin**. No Android imports, no Retrofit imports, no Compose imports.
* **Use Cases:** Every business action must be a single Use Case (e.g., `GetCharactersUseCase`). They must override the `operator fun invoke()` and return a `Flow<NetworkResult<T>>` or data.

### 3.2 Data Layer (Data Management)
* **Retrofit:** All network calls happen here using Retrofit interfaces.
* **Mappers:** Never expose API DTOs (Data Transfer Objects) to the Domain or UI layers. Convert them into Domain Models in this layer using mapper extension functions.

### 3.3 Presentation Layer (UI)
* **Jetpack Compose:** The UI must be built 100% using Jetpack Compose. No XML layouts.
* **ViewModels:** ViewModels communicate with Use Cases, collect Flows, and expose UI State via a single `StateFlow`. They must never hold references to Android Views or Contexts.

---

## 4. Network & Error Handling Strategy

### 4.1 Internet Connectivity Check
Before making any network request, a custom network interceptor or utility must verify internet connectivity. If offline, it must immediately yield a specific connectivity exception without hitting the server.

### 4.2 Network Result Wrapper
All API results and errors must be mapped to a sealed interface/class to provide clear UI feedback:

```kotlin
sealed interface NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>
    data class Error(val exception: Throwable, val message: String) : NetworkResult<Nothing>
    object Loading : NetworkResult<Nothing>
}
````


### 4.3 UI Feedback
ViewModels must catch both pre-request errors (like No Internet) and post-request errors (Server errors, timeouts) via the NetworkResult.Error wrapper.

The UI state must expose these errors clearly so the Compose views can render Error Screens, Dialogs, or Snackbars gracefully.

## 5. Coding Standards for the Agent
State Management: Use mutableStateOf inside Composables only for local UI state (e.g., expanding a card). Global/Screen state belongs to the ViewModel's StateFlow.

Preview: Always provide a @Preview composable with mock data for every screen component.

Dependency Injection: Use the established DI framework of the project (or manual constructor injection if specified) to provide dependencies. Never instantiate Repositories or Use Cases inside ViewModels directly.

No Over-engineering: Keep it simple. Do not create interfaces for Use Cases or ViewModels unless strictly necessary. Interfaces are mandatory ONLY for Repositories (to separate Domain from Data).