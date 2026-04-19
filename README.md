# 🏃‍♂️ Runique - Modern Fitness Tracker

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack_Compose-Latest-green.svg?style=flat&logo=android)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/Architecture-Clean_/_Multi--Module-orange.svg?style=flat)]()
[![License](https://img.shields.io/badge/License-MIT-red.svg?style=flat)]()

**Runique** is a production-ready, high-performance fitness tracking application built to demonstrate modern Android development excellence. It focuses on scalability through a robust multi-module architecture, an offline-first data strategy, and a highly reactive UI.

---

## 🚀 Key Features

- **📍 Real-time Tracking:** Precise run tracking using GPS and sensor fusion.
- **📊 Advanced Analytics:** Deep insights into performance with custom-built data visualization (Line Charts).
- **💾 Offline-First:** Full functionality without internet connectivity using a local-first caching strategy.
- **🔐 Secure Auth:** Robust authentication flow including session management and secure token storage.
- **📦 Dynamic Delivery:** On-demand feature loading (Analytics) to minimize initial app size.
- **🎨 Material 3 Design:** Beautiful, adaptive UI with a custom-built design system.

---

## 🛠 Tech Stack

### UI & Presentation
- **Jetpack Compose:** 100% declarative UI.
- **Material Design 3:** Latest design components and theming.
- **Compose Navigation:** Type-safe navigation between features.
- **Coil:** Efficient image loading for Compose.

### Core & Logic
- **Kotlin Coroutines & Flow:** Reactive programming for asynchronous tasks.
- **Koin:** Lightweight and efficient dependency injection.
- **WorkManager:** Reliable background data synchronization.
- **Ktor:** Modern, asynchronous HTTP client for network requests.

### Data & Persistence
- **Room Database:** Local SQLite persistence following the "Local Source of Truth" pattern.
- **EncryptedSharedPreferences:** Secure storage for sensitive user data.

---

## 🏗 Architecture

Runique follows **Clean Architecture** principles combined with a **Multi-Module** approach (19+ modules). This ensures that the project remains scalable, testable, and maintainable.

### Module Structure
The project is divided into logical layers:
- **`:app`**: The entry point, stitching together all feature modules.
- **`:auth`**: Handles registration, login, and session management.
- **`:run`**: Core run tracking and mapping logic.
- **`:analytics`**: **(Dynamic Feature)** Visualizes user performance data.
- **`:core`**: Shared logic, database, network, and the Design System.

### Data Flow
The app implements a **Unidirectional Data Flow (UDF)** using **MVI/MVVM** patterns:
`UI (View) -> Action -> ViewModel -> State -> UI (View)`

---

## 🛠 Build System (Convention Plugins)

One of the project's highlights is its advanced build infrastructure. Instead of duplicating Gradle logic, Runique uses **Custom Convention Plugins** located in the `build-logic` module:

- `AndroidApplicationComposeConventionPlugin`: Sets up Compose for apps.
- `AndroidLibraryConventionPlugin`: Standardizes library module configurations.
- `AndroidRoomConventionPlugin`: Centralized Room setup with KSP.
- `AndroidFeatureUiConventionPlugin`: Pre-configures UI-heavy feature modules.

This approach reduces `build.gradle.kts` boilerplate by **over 70%**.

---

## 📥 Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/Runique.git
   ```
2. Open the project in **Android Studio (Ladybug or newer)**.
3. Sync Gradle and ensure you have the latest **Kotlin 1.9+** and **JDK 17+**.
4. (Optional) Set up your API keys in `gradle.properties` if required for mapping services.
5. Hit **Run**! 🚀

---

## 📸 Screenshots

| Auth Flow | Run Tracking | Analytics |
| :---: | :---: | :---: |
| ![Auth](https://via.placeholder.com/200x400?text=Login+Screen) | ![Tracking](https://via.placeholder.com/200x400?text=Map+Screen) | ![Analytics](https://via.placeholder.com/200x400?text=Chart+Screen) |


