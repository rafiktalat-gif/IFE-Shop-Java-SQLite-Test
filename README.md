# IFE Shop — Native Java SQLite Test

This is a minimal Cordova test project for native Android SQLite persistence.

## Database

The Java plugin creates `ife-shop.db` automatically in Android's private application database storage. No database file is bundled with the web assets.

The table is created automatically with:

```sql
CREATE TABLE IF NOT EXISTS items (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL
);
```

## Build

Push the project to GitHub. GitHub Actions builds a debug APK using:

- Cordova CLI 13
- cordova-android 15.1.0
- JDK 17
- Android API 36 / Build Tools 36.0.0

The APK is uploaded as a workflow artifact.

## Test

1. Install the APK.
2. Enter a name and press SAVE.
3. Add several records.
4. Close the app completely.
5. Open it again.
6. Press REFRESH if necessary.
7. The same records should still be present.

`CLEAR ALL` is included only for testing.

## Browser note

This project is intended to run as a Cordova Android app. The native Java SQLite bridge is not available when `index.html` is opened directly from localhost in an ordinary browser.
