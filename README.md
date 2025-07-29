# User Management App (Android)

An Android application that allows users to **register**, **log in**, and **view user data** based on their roles (admin or normal user). The app integrates with **Firebase Authentication** and **Firebase Realtime Database** and includes a **background service** to fetch user data and display it via broadcasts.

---

## ✨ Features

* 🔐 **User Authentication** (via Firebase Auth)
* 🗂️ **User Data Storage** in Firebase Realtime Database
* 🌽 **Role-based View Control**

  * **Admin** users see all users.
  * **Normal** users only see their own details.
* 🔄 **Background ForegroundService** (`UserSyncService`)

  * Fetches data and broadcasts updates
* 📡 **Broadcast Receiver** for live updates on UI
* 📱 **SharedPreferences** used for session management

---

## 🏗️ Tech Stack

* **Kotlin / Java**
* **Firebase Authentication**
* **Firebase Realtime Database**
* **Android Services & BroadcastReceiver**
* **Shared Preferences**
* **ListView, TextView UI components**

---

## 📁 Project Structure

```
com.example.usermanagementapp/
├── LoginActivity.kt            # Handles login using Firebase Auth
├── RegisterActivity.kt         # Handles user registration
├── WelcomeActivity.kt          # Entry point after login
├── UserListActivity.kt         # Displays user(s) info based on role
├── UserSyncService.kt          # Foreground service fetching and broadcasting data
└── layout/
    ├── activity_login.xml
    ├── activity_register.xml
    ├── activity_user_list.xml
    └── activity_welcome.xml
```

---

## 🔑 Roles

* Role is determined at registration and stored under `role` key in Firebase.
* Stored locally in `SharedPreferences` under `userType`.

---

## 🚀 How It Works

1. **LoginActivity** authenticates the user and retrieves `role`.
2. Role and username are saved locally.
3. **WelcomeActivity** redirects to `UserListActivity`.
4. **UserListActivity** starts `UserSyncService`.
5. Service fetches:

   * all users if admin
   * current user only if normal
6. Service broadcasts data via `Intent`.
7. `BroadcastReceiver` receives and updates the UI.

---

## 🦚 Testing Notes

* ✅ Admin view tested and shows all users correctly.
* ✅ Normal user view tested and shows own details.
* ✅ Broadcasts function properly on Android 12+ with `setPackage(...)` fix.
* ✅ Role stored correctly and displayed in UI.

---

## ❗ Known Issues / Fixes

* ⛔ **Broadcasts not received** on Android 12+ → ✅ Fixed by using `intent.setPackage(packageName)`
* ⛔ **Role showing as N/A** → ✅ Fixed by fetching `role` from Firebase properly
* ⛔ **Normal users not seeing data** → ✅ Fixed by verifying `auth.currentUser?.uid` before accessing

---

## 📌 Requirements

* Android Studio Bumblebee or later
* Android SDK 31+
* Firebase account and project
* Internet connection (required for Firebase)

---

## 📥 Setup Instructions

1. Clone the repo or import into Android Studio.
2. Connect your app to Firebase (via Tools > Firebase).
3. Set your Firebase Realtime DB rules (for development):

```json
{
  "rules": {
    ".read": "auth != null",
    ".write": "auth != null"
  }
}
```

4. Update the Firebase URL in `UserSyncService.kt`:

```kotlin
FirebaseDatabase.getInstance("https://<your-db>.firebaseio.com/")
```

5. Run on emulator or physical device.

---

## 🧑‍💻 Author

**Medha Shree**
GitHub: [@Medha170](https://github.com/Medha170) 

---

## 📝 License

This project is for educational/demo purposes and not intended for production use.
