# Medicine Reminder App

A modern, intuitive Android application designed to help users track their medications, set customizable schedules, and receive timely notifications to ensure doses are never missed.

## Features

*   **Custom Reminders:** Schedule daily, weekly, or specific interval notifications for various medications.
*   **Dosage & Unit Tracking:** Specify dosage amounts, measurement units (e.g., mg, ml, pills), and intake instructions (e.g., before/after meals).
*   **Inventory Alerts:** Keep track of remaining pill counts and receive warnings when stock is running low.
*   **History Logs:** View logs of taken, skipped, or missed medications to monitor compliance over time.
*   **User-Friendly Interface:** Built with clean Material Design guidelines for seamless navigation.

## Tech Stack

*   **Language:** Kotlin
*   **Architecture:** MVVM (Model-View-ViewModel)
*   **UI Framework:** Jetpack Compose / Android XML Views
*   **Database:** Room Database (Local Persistence)
*   **Background Tasks:** WorkManager / AlarmManager (for precise notifications)
*   **Build System:** Gradle

## Prerequisites

Before building the project, ensure you have:
*   **Android Studio:** Ladybug (2024.2.1) or newer recommended
*   **JDK:** Version 17 or higher
*   **Android SDK:** Minimum API Level 24 (Android 7.0) or higher

## Installation & Setup

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/your-username/MedicineReminderApp.git](https://github.com/your-username/MedicineReminderApp.git)
   
## Project structure
   MedicineReminderApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/medicinereminderapp/
│   │   │   │   ├── data/          # Room DB, Entities, DAOs, Repositories
│   │   │   │   ├── ui/            # UI Layer (Screens, ViewModels, Components)
│   │   │   │   └── notification/  # Alarm & Notification Handling
│   │   │   ├── res/               # Layouts, Drawables, Strings, Values
│   │   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
└── settings.gradle.kts
