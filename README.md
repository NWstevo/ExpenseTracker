# Expense Tracker (Android)

A local-only, privacy-friendly money accountability app: log expenses manually
or with a receipt photo, and see weekly/monthly totals against a budget with
a warning threshold and an overdraft allowance.

## What's built

- **Add expenses** manually (amount, category, note) or attach a receipt
  photo taken with the system Camera app.
- **Weekly & monthly totals**, auto-calculated (Monday–Sunday weeks,
  calendar months) and broken down by category.
- **Budget settings**: separate weekly and monthly limits, a warning
  threshold (% of the limit that triggers a "heads up"), and an overdraft
  allowance (how far past the limit you're still willing to tolerate before
  it's flagged as exceeded).
- **All data stays on the device** in a local Room (SQLite) database — no
  account, no network calls, nothing leaves the phone.

## Project structure

```
app/src/main/java/com/expensetracker/app/
├── data/            Room entities, DAOs, database, repository
├── util/            Date-range math, budget-status calculation
├── camera/          Receipt photo capture helper
├── viewmodel/        ExpenseViewModel — the single source of UI state
└── ui/
    ├── theme/        Compose colors/typography/theme
    ├── navigation/    Bottom nav + screen routing
    ├── screens/       Dashboard, History, Add Expense, Budget Settings
    └── components/    Reusable pieces (progress card, list item, bar chart)
```

## Key terms, if you want to look them up

- **Jetpack Compose**: Android's modern UI toolkit — you describe *what* the
  screen should look like as functions (`@Composable`), and it re-renders
  automatically when the underlying state changes. No more manually calling
  `findViewById` or writing XML layouts.
- **Room**: a library that sits on top of SQLite (Android's built-in
  database engine). You define `@Entity` data classes (tables) and `@Dao`
  interfaces (queries), and Room generates the SQL-executing code for you.
- **DAO (Data Access Object)**: an interface listing the exact database
  operations allowed (insert, delete, "sum amounts between two dates", etc).
  Keeping all SQL behind DAOs means the rest of the app never writes raw SQL.
- **Flow / StateFlow**: Kotlin's reactive stream types. A `Flow<List<Expense>>`
  from Room automatically emits a new list whenever the table changes — the
  UI collects it and redraws itself, with no manual "refresh" step.
- **ViewModel**: an Android class that holds UI state and survives
  configuration changes (like screen rotation). `ExpenseViewModel` is the one
  place all screens go to read data or trigger a database write.
- **FileProvider**: an Android mechanism for safely sharing a file (like a
  photo) between your app and another app (the Camera app) without exposing
  raw file paths, using `content://` URIs instead.

## Camera approach — a deliberate trade-off

Instead of embedding a full CameraX preview inside the app, receipt photos
are captured by launching the phone's own Camera app via an
`ACTION_IMAGE_CAPTURE`-style intent (`ActivityResultContracts.TakePicture()`).

Trade-off: you don't get a custom in-app camera UI, but you also don't need
to request the `CAMERA` runtime permission (the Camera app already has it),
don't need to manage a camera preview lifecycle, and skip a fairly heavy
CameraX dependency. For "attach a photo of a receipt," that's a good deal.

## Budget logic

`util/BudgetStatus.kt` is the core accountability logic. Given a spent
amount, a limit, a warning threshold %, and an overdraft allowance, it
computes one of four levels:

1. **SAFE** — under the warning threshold.
2. **WARNING** — past the warning threshold, still under the limit.
3. **OVER_LIMIT** — past the limit, but still within the overdraft allowance.
4. **OVERDRAFT_EXCEEDED** — past limit + overdraft allowance entirely.

Example: monthly limit = 500, overdraft allowance = 100, warning threshold =
80%. Spending 420 → WARNING (84% of 500). Spending 550 → OVER_LIMIT (past
500, still under the 600 ceiling). Spending 650 → OVERDRAFT_EXCEEDED.

This same function drives both the weekly and monthly progress cards on the
Dashboard — they just get called with different totals and limits.

## Opening and running the project

1. Install **Android Studio** (Koala/2024.1 or newer) if you don't have it.
2. Open the `ExpenseTracker` folder as a project (File → Open).
3. Android Studio will offer to generate the Gradle wrapper jar and sync
   automatically — accept that. (The wrapper *properties* file is included,
   pointing at Gradle 8.6, but the wrapper `.jar` binary itself isn't, since
   it's a binary file — Android Studio regenerates it on first sync.)
4. Once sync finishes, run on an emulator or a physical device (`Run ▶`).
   Minimum supported Android version is **API 26 (Android 8.0)**.

If Gradle sync complains about a missing Android SDK platform, use
**Tools → SDK Manager** to install "Android 14.0 (API 34)" and the matching
build tools — Android Studio usually prompts for this automatically.

## Known limitations / good next steps

- No OCR: photo is attached as-is, amount/category are still typed in. Adding
  ML Kit text recognition to auto-fill the amount from the receipt is a
  natural next step if you want to practice on-device ML.
- No push notifications when a budget is exceeded — the warning only shows
  when you open the app. Could be added with `WorkManager` running a daily
  check.
- No recurring/subscription expenses, no multi-currency support, no CSV
  export yet.
- No automated tests included — a good exercise would be unit-testing
  `BudgetStatus` (it's pure logic, no Android dependencies, so it's the
  easiest thing in the project to test).

## Verification performed in this session

I don't have an Android SDK or emulator available in this sandbox, so I
couldn't run a full Gradle build here. Instead I checked: every file's
braces/parens balance, every package declaration matches its file path,
and every screen referenced in the navigation graph has a matching
`@Composable` function defined. I'd still recommend doing a full Gradle
sync in Android Studio as your first step — that's the authoritative
compiler check.
