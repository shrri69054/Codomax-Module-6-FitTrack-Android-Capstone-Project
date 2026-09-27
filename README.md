# FitTrack — Mobile App Capstone Project

A fitness tracker Android app built with Kotlin + Jetpack Compose, covering every
requirement of the Module 6 capstone brief:

- ✅ Navigation (Jetpack Navigation Compose)
- ✅ Logic patterns (MVVM + Repository pattern)
- ✅ Local database (Room)
- ✅ Cloud authentication (Firebase Auth)
- ✅ Hardware integration (device step-counter sensor)
- ✅ Documented architecture, data model, and screen flow (below)

---

## 1. App Overview

FitTrack lets a user register/log in, log workouts (type, duration, calories,
steps), see a live step count read directly from the phone's hardware sensor,
and review workout history — all persisted locally and tied to their cloud
account.

## 2. Architecture

```
UI (Jetpack Compose screens)
        │
ViewModel (WorkoutViewModel, AuthViewModel)   ← MVVM
        │
Repository (WorkoutRepository)                ← Repository pattern
        │
Room DAO/Database  ─────────────  Firebase Auth (cloud)
        │
SQLite (local persistence)
```

**Pattern used:** MVVM (Model-View-ViewModel) with a Repository layer that
abstracts the data source (Room today; swappable for a remote API later)
from the ViewModel, which in turn exposes `StateFlow` to stateless
Compose UI screens.

## 3. Data Model (ER diagram, text form)

```
User (Firebase Auth)
  uid (PK, string)
  email

Workout (Room table: "workouts")
  id (PK, autoincrement)
  userId (FK → User.uid)
  type            e.g. Running, Cycling, Yoga
  durationMinutes
  caloriesBurned
  steps
  dateMillis
```

## 4. Screen / Navigation Flow (wireframe map)

```
[Login] ──(no account)──▶ [Register] ──▶ [Home]
   │                                        │
   └────────────(success)──────────────────▶│
                                             │
                     [Home] ──(+)──▶ [Add Workout] ──(save)──▶ back to Home
                        │
                        └──(profile icon)──▶ [Profile] ──(logout)──▶ [Login]
```

- **Login / Register**: email + password fields, Firebase Auth calls.
- **Home**: live sensor step card, aggregate stats (total steps/calories),
  scrollable workout history list, FAB to add a workout.
- **Add Workout**: simple form → writes to Room via the ViewModel.
- **Profile**: shows user id, logout button.

## 5. Project Structure

```
app/src/main/java/com/example/fittrack/
├── MainActivity.kt
├── nav/NavGraph.kt              # navigation graph
├── auth/AuthViewModel.kt        # Firebase cloud auth
├── sensor/StepCounterManager.kt # hardware sensor integration
├── data/
│   ├── local/                   # Room entity, DAO, database
│   └── repository/              # Repository pattern
├── viewmodel/WorkoutViewModel.kt
└── ui/
    ├── screens/                 # Compose screens
    └── theme/                   # Material3 theme
```

## 6. Setup Instructions

1. Open the project folder in **Android Studio** (Koala or later).
2. Create a Firebase project at https://console.firebase.google.com, add an
   Android app with package name `com.example.fittrack`, and download
   `google-services.json` into the `app/` folder (this file is gitignored —
   each developer/grader adds their own, or you can commit a shared one for
   submission if your instructor requires it).
3. Enable **Email/Password** sign-in under Firebase Authentication.
4. Sync Gradle and run on a device/emulator (Android 7.0 / API 24+).
5. Grant the "Physical activity" permission when prompted to enable the live
   step counter card.

## 7. Building an APK (for submission)

```
./gradlew assembleRelease
```
Output APK: `app/build/outputs/apk/release/app-release.apk`

## 8. Submission Deliverables Checklist

- [ ] GitHub Repository Link (see git commands below)
- [ ] Demo video walking through Login → Home → Add Workout → Profile → Logout
- [ ] Live Website Link (optional landing page, or Firebase Hosting demo page)
- [ ] LinkedIn Post Link

---

## 9. Git Commands — Commit & Push to GitHub

Run these from inside the `FitTrack/` project folder.

### A. First-time setup (new repo)

```bash
# 1. Initialize git in this folder
git init

# 2. Set your identity (skip if already configured globally)
git config user.name "Your Name"
git config user.email "you@example.com"

# 3. Stage all files
git add .

# 4. Commit
git commit -m "Initial commit: FitTrack Android capstone project"

# 5. Set main as the default branch name
git branch -M main

# 6. Create a new EMPTY repo on GitHub first (github.com/new), then link it:
git remote add origin https://github.com/<your-username>/FitTrack.git

# 7. Push
git push -u origin main
```

### B. Every time you make further changes

```bash
git add .
git commit -m "Describe what you changed"
git push
```

### C. Using GitHub CLI instead (optional, creates the repo for you)

```bash
gh repo create FitTrack --public --source=. --remote=origin --push
```

### D. If you prefer SSH over HTTPS

```bash
git remote set-url origin git@github.com:<your-username>/FitTrack.git
```

---

## 10. Next Steps to Extend (optional, for extra credit)

- Add Google Sign-In alongside email/password.
- Add a Firestore sync so workouts back up to the cloud, not just Room.
- Add charts (e.g. MPAndroidChart or Compose Charts) for weekly trends.
- Add unit tests for `WorkoutRepository` using a fake DAO.
