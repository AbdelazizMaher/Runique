# 🏃‍♂️ Runique - Modern Fitness Tracker

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack_Compose-Latest-green.svg?style=flat&logo=android)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/Architecture-Clean_/_Multi--Module-orange.svg?style=flat)]()
[![Build System](https://img.shields.io/badge/Build_Logic-Convention_Plugins-purple.svg?style=flat)]()

**Runique** is a high-performance, production-ready fitness application designed to demonstrate senior-level Android engineering practices. It utilizes a **19-module** architecture, custom **Gradle Convention Plugins**, and a robust **Offline-First** strategy to provide a seamless, scalable tracking experience.

---

## 📖 Table of Contents
- [Architecture](#-architecture)
- [Module Structure](#-module-structure)
- [Build Logic & Convention Plugins](#-build-logic--convention-plugins)
- [Tech Stack](#-tech-stack)
- [Key Features](#-key-features)
- [Database & Offline Strategy](#-database--offline-strategy)
- [Dynamic Delivery](#-dynamic-delivery)
- [Design System](#-design-system)

---

## 🏗 Modular Architecture & Engineering Design

Runique is engineered with a **Layered Multi-Module Architecture**, which is the gold standard for large-scale, maintainable Android applications. This structure enforces a strict separation of concerns, improves build times through parallelization, and ensures high testability.

### 🗺 High-Level Dependency Graph

The project is split into vertical **Features** and horizontal **Core** infrastructure. Dependencies flow strictly inwards toward the **Domain** layers and downwards toward **Core** infrastructure, preventing circular dependencies and leaky abstractions.

```mermaid
graph TB
    subgraph "App Layer (Orchestration)"
        app[":app"]
    end

    subgraph "Feature Layers (Vertical Slices)"
        direction TB
        subgraph ":run"
            run_p[":run:presentation"]
            run_d[":run:data"]
            run_m[":run:domain"]
        end
        subgraph ":auth"
            auth_p[":auth:presentation"]
            auth_d[":auth:data"]
            auth_m[":auth:domain"]
        end
        subgraph ":analytics (Dynamic)"
            ana_f[":analytics:analytics_feature"]
            ana_p[":analytics:presentation"]
            ana_d[":analytics:data"]
            ana_m[":analytics:domain"]
        end
    end

    subgraph "Core Layers (Horizontal Infrastructure)"
        direction LR
        c_ui[":core:presentation:ui"]
        c_ds[":core:presentation:designsystem"]
        c_data[":core:data"]
        c_db[":core:database"]
        c_net[":core:network"]
        c_dom[":core:domain"]
    end

    %% App orchestration
    app --> run_p & auth_p & ana_f
    ana_f --> ana_p

    %% Feature Internal & Core Dependencies
    run_p & auth_p & ana_p --> c_ui
    c_ui --> c_ds
    
    run_d & auth_d & ana_d --> c_data
    c_data --> c_db & c_net
    
    run_m & auth_m & ana_m --> c_dom
    c_data --> c_dom

    %% Styling
    style app fill:#f9f,stroke:#333,stroke-width:2px
    style ana_f fill:#bbf,stroke:#333,stroke-dasharray: 5 5
```

### 🧱 Architectural Principles

1.  **Clean Architecture:** Strict separation between business logic (Domain), data handling (Data), and UI (Presentation).
2.  **Feature Vertical Slicing:** Features like `Auth`, `Run`, and `Analytics` are self-contained vertical slices. They don't know about each other, making the codebase highly pluggable.
3.  **Domain-Driven Design (DDD):** Every feature has a pure Kotlin `:domain` module with zero Android dependencies, enabling lightning-fast unit testing.
4.  **Local Source of Truth:** The UI always observes the local database (`Room`), which is synchronized with the remote API (`Ktor`) in the background.

### 🔄 Reactive Data Flow (UDF)

Runique implements a strict **Unidirectional Data Flow** pattern. State is managed in ViewModels and exposed as reactive streams to the UI.

```mermaid
sequenceDiagram
    participant UI as Compose UI
    participant VM as ViewModel
    participant UC as Use Case / Domain
    participant Repo as Repository
    participant DB as Room (Local)

    UI->>VM: User Action (e.g., Start Run)
    VM->>UC: Process Business Logic
    UC->>Repo: Request Data Update
    Repo->>DB: Update Local Storage
    DB-->>Repo: Emit New State (Flow)
    Repo-->>VM: Update UI State
    VM-->>UI: Recompose Screen
```

### ⚖️ Dependency Rules
*   **Strict Layering:** Presentation and Data modules can only interact with Domain modules. They are never allowed to depend on each other directly.
*   **No Cross-Feature Dependencies:** Features are entirely decoupled. Shared functionality must be moved to a `:core` module.
*   **Pure Kotlin Domain:** To ensure platform independence and test speed, domain modules must not contain any Android Framework dependencies.

---

## 📂 Module Structure

The project is highly modularized (19+ modules) to facilitate parallel development and faster build times.

| Module Group | Modules | Description |
| :--- | :--- | :--- |
| **App** | `:app` | Entry point, DI initialization, and navigation host. |
| **Auth** | `:auth:domain`, `:auth:data`, `:auth:presentation` | User registration, login, and secure session management. |
| **Run** | `:run:domain`, `:run:data`, `:run:presentation` | Core tracking logic, GPS integration, and map visualization. |
| **Analytics** | `:analytics:domain`, `:analytics:data`, `:analytics:presentation`, `:analytics:analytics_feature` | Dynamic feature for performance stats and custom charting. |
| **Core** | `:core:domain`, `:core:data`, `:core:database`, `:core:presentation:designsystem`, `:core:presentation:ui` | Shared logic, Design System, Local DB, and Network client. |
| **Build Logic** | `:build-logic:convention` | Custom Gradle plugins for project-wide standardization. |

---

## 🛠 Build Logic & Convention Plugins

Instead of repetitive `build.gradle.kts` files, Runique uses **Custom Convention Plugins** to enforce consistency:

- **`AndroidApplicationComposeConventionPlugin`**: Standardizes Compose setup, compiler flags, and dependencies for app modules.
- **`AndroidLibraryConventionPlugin`**: Configures common Android library settings.
- **`AndroidRoomConventionPlugin`**: Centralizes Room setup, KSP configurations, and schema exports.
- **`AndroidFeatureUiConventionPlugin`**: Automatically adds Compose, Design System, and UI dependencies to feature modules.
- **`AndroidDynamicFeatureConventionPlugin`**: Configures modules for on-demand delivery.

This approach reduces `build.gradle.kts` boilerplate by **over 70%**.

---

## 🚀 Tech Stack

- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) with [Material Design 3](https://m3.material.io/).
- **Navigation:** [Compose Navigation](https://developer.android.com/jetpack/compose/navigation) (Type-safe).
- **DI:** [Koin](https://insert-koin.io/) for lightweight, multi-module dependency injection.
- **Networking:** [Ktor Client](https://ktor.io/) with Content Negotiation (Kotlinx Serialization).
- **Database:** [Room](https://developer.android.com/training/data-storage/room) with **KSP** for local persistence.
- **Async:** Kotlin Coroutines & [Flow](https://kotlinlang.org/docs/flow.html) for reactive data streams.
- **Images:** [Coil](https://coil-kt.github.io/coil/) for asynchronous image loading.
- **Background:** [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) for reliable data syncing.

---

## ✨ Key Features

### 📍 Precise Run Tracking
- **GPS & Sensor Fusion:** Real-time run tracking with fused location providers.
- **Interactive Maps:** Integration with mapping services to visualize running routes.
- **Foreground Services:** Reliable tracking that persists even when the app is in the background.

### 📊 Custom Data Visualization
- **Bespoke Line Chart:** A custom-built Compose component for visualizing pace, distance, and elevation over time.
- **Real-time Analytics:** Processing and displaying performance metrics on the fly.

### 🛡 Secure Authentication
- **Full Auth Flow:** Registration, login, and logout with rigorous validation.
- **Secure Persistence:** Using **EncryptedSharedPreferences** for token management.
- **Session Handling:** Automatic session refresh and secure navigation guards.

---

## 💾 Database & Offline Strategy

Runique follows a **Local Source of Truth** strategy to ensure a premium user experience regardless of connectivity.
1.  **Write Path:** UI sends data to the Repository -> Repository saves to **Room**.
2.  **Read Path:** UI observes a reactive **Flow** from the Room database.
3.  **Sync Path:** A background **WorkManager** job periodically synchronizes local data with the remote **Ktor** API.
4.  **Reliability:** In case of network failure, the user sees their data immediately; synchronization happens automatically when the connection returns.

---

## 📦 Dynamic Delivery

The `:analytics:analytics_feature` is configured as an **Android Dynamic Feature Module**. This allows:
- **Reduced App Size:** Users only download the analytics suite when they actually need it.
- **On-Demand Loading:** Demonstrates mastery of advanced Google Play Store delivery mechanisms.

---

## 🎨 Design System

Located in `:core:presentation:designsystem`, the app features a fully custom theme:
- **RuniqueTheme:** Custom colors, typography, and shapes tailored for a fitness app.
- **Reusable UI Components:** Standardized Buttons, TextFields, and Cards used across all modules to ensure visual consistency.
- **Adaptive Design:** UI that scales and adapts to different screen sizes and dark/light modes.

---

## 📸 Screenshots

<!-- 
TIP: To get GitHub-hosted URLs without adding images to your local project:
1. Open a 'New Issue' in your GitHub repo.
2. Drag and drop your screenshots into the comment box.
3. Copy the generated URLs (e.g., https://github.com/user/repo/assets/...)
4. Replace the 'PASTE_URL_HERE' placeholders below.
-->

| Splash Screen | Intro Screen | Login Screen | Register Screen | Run Overview | Active Run | Analytics Dashboard |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/c00c176c-d338-439e-9866-42ec03c97114" width="200" /> | <img src="https://github.com/user-attachments/assets/b329c9f8-856c-4df8-b24d-ca8ae9b72cd3" width="200" /> | <img src="https://github.com/user-attachments/assets/393da88c-68b7-4bc1-be9f-feb20e187c3b" width="200" /> | <img src="https://github.com/user-attachments/assets/64cefe21-130f-4499-acc2-18a70f9a4c92" width="200" /> | <img src="PASTE_ANALYTICS_URL_HERE" width="200" /> | <img src="PASTE_ANALYTICS_URL_HERE" width="200" /> | <img src="PASTE_ANALYTICS_URL_HERE" width="200" /> |

---

## 📥 Installation & Setup

1.  Clone the repository: `git clone https://github.com/your-username/Runique.git`
2.  Open in **Android Studio (Ladybug or newer)**.
3.  Ensure you have **JDK 17** or higher configured.
4.  Sync Project with Gradle Files.
5.  Run the `:app` configuration.

---

*Built with a commitment to clean code, scalability, and the latest Android development standards.*
