# 💰 Money_Up – Personal Finance & Budget Tracker

**Money_Up** is an Android application developed in Kotlin using Android Studio and Room Database. The app helps users track their monthly budgets, log daily expenses, filter spending within specific timeframes, and categorize expenses with descriptions and receipt images.

---

## 🚀 Features

- 🧾 **Add Expenses** with name, amount, date, time, description, category, and photo.
- 📆 **Filter by Date Range** to view expenses during a user-selectable period.
- 📂 **Category-Based Expense Summary** – See total money spent per category during a selected period.
- 📸 **Attach Photos** to expense entries (e.g., receipts or bills).
- 💼 **Monthly Budget Management** – Set minimum and maximum budget per month.
- 📊 **Total Budget Calculation** per user.
- 👤 **User Profile** support (with future login/registration capability).
- 🔄 **Live Data with Flow** – Real-time updates using Kotlin coroutines and Flow.
- 📱 **Bottom Navigation** – Seamless navigation between Home, Budget, Expenses, Profile, and Settings.

---

## 🛠️ Tech Stack

- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **Persistence**: Room Database (SQLite)
- **Async Handling**: Kotlin Coroutines + Flow
- **UI**: Material Design Components
- **Tools**: Android Studio, Jetpack Libraries

---
# how to run the app from a zipped folder:


To run the **Money_Up** Android app from a zipped project folder, first extract the contents of the `.zip` file to a location on your computer.
Open **Android Studio**, click **"Open"**, and navigate to the extracted folder — make sure to select the main project directory (where the `build.gradle` or `settings.gradle` file is located)
. Once opened, Android Studio will begin syncing the Gradle files; if it doesn’t happen automatically, you can trigger it manually via **File > Sync Project with Gradle Files**.
After syncing completes, connect your Android device or start an emulator, then click the green **Run** ▶ button at the top of Android Studio to build and launch the app.
Make sure you have a stable internet connection during the first launch to download any missing dependencies.
If you encounter issues related to Room database migrations (such as missing columns), uninstall the app from the emulator or device and rebuild it to reset the database.



