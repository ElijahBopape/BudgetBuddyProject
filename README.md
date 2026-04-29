# BudgetBuddy

A budgeting application built using Kotlin in Android Studio. The app allows users to track expenses, manage categories, and monitor monthly spending against budget goals.

---

## Features

### Authentication
- User registration and login (SQLite)
- Session management (automatic login)
- Logout functionality

### Dashboard
- Displays total monthly spending
- Shows budget status (min/max goals)
- Quick navigation to core features

### Add Expense
- Add title, amount, date, and category
- Capture image using camera (FileProvider)
- Select image from gallery
- Input validation (non-empty, positive values)

### Expense List
- Displays all expenses using RecyclerView
- Reverse chronological order
- Optional date range filtering

### Manage Categories
- View all categories
- Add new categories (no duplicates)

### Category Report
- Total spending per category
- Optional date filtering

### Budget Goals
- Set minimum and maximum monthly budget
- Stored using SharedPreferences
- Reflected in dashboard

### Photo Attachments
- Optional image per expense
- Camera and gallery support
---
## Tech Stack

- Kotlin
- Android Studio
- SQLite (SQLiteOpenHelper)
- SharedPreferences
- Material Design 3
- Android SDK (API 21 - API 34)

- com.budgetbuddy
├── adapter
│ └── ExpenseAdapter.kt
├── data
│ └── DBHelper.kt
├── ui
│ ├── AddExpenseActivity.kt
│ ├── CategoryReportActivity.kt
│ ├── ExpenseListActivity.kt
│ ├── LoginActivity.kt
│ ├── MainActivity.kt
│ └── ManageCategoriesActivity.kt
└── utils
└── SessionManager.kt


---

## Setup

1. Open the project in Android Studio  
2. Allow Gradle to sync  
3. Run on emulator or physical device  

### Requirements
- Android Studio (Hedgehog or newer)
- Android SDK installed
- Minimum SDK: API 21  
- Target SDK: API 34  

---

## Dependencies


androidx.core:core-ktx
androidx.appcompat:appcompat
com.google.android.material:material
androidx.constraintlayout:constraintlayout
androidx.cardview:cardview
androidx.recyclerview:recyclerview


---

## APK

The APK file is included in the submission folder and can be used to install the application directly.

---

## Demo Video

https://youtu.be/TiRHhRwBKRY


## References

- Android Developer Documentation  
  https://developer.android.com/

- Kotlin Documentation  
  https://kotlinlang.org/docs/home.html

- Material Design Guidelines  
  https://m3.material.io/

- SQLite Android Guide  
  https://developer.android.com/training/data-storage/sqlite

- RecyclerView Guide  
  https://developer.android.com/guide/topics/ui/layout/recyclerview

- Camera and FileProvider  
  https://developer.android.com/training/camera

- SharedPreferences  
  https://developer.android.com/reference/android/content/SharedPreferences

---

## Notes

- Built and tested using Android Studio  
- Uses Android SDK tools and standard AndroidX libraries  
- Designed with a blue and white Material theme  

---

## Project Structure
