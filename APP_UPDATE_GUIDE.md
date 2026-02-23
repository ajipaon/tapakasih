# Panduan Update App EMoneyChecker

## Langkah 1: Buat Application Class

Buat file baru di app Anda:

```
app/src/main/java/com/paondev/emoneychecker/MyApplication.kt
```

Isi dengan kode berikut:

```kotlin
package com.paondev.emoneychecker

import android.app.Application
import com.paondev.lib.tapakasih.TapakAsih
import com.paondev.lib.tapakasih.config.TapakAsihConfig

/**
 * Application class for EMoneyChecker
 * Handles SDK initialization and app-wide setup
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize TapakAsih SDK
        val config = TapakAsihConfig.Builder(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0aWNrZXRfaWQiOiJhZDMwODBlZC05MDFkLTRhOTQtOTIzOC1kNzFjNGZmNTQ4ZjYiLCJ0aWNrZXRfbmFtZSI6ImVtb25leSBjaGVja2VyIiwicGFja2FnZV9uYW1lIjoiY29tLnBhb25kZXYuZW1vbmV5Y2hlY2tlciIsInVzZXJfaWQiOiI1OTExMDA0Yy01YzVjLTQ5NGQtYjY1NS1kZDBlNzgxZGZjYjgiLCJyZXF1ZXN0ZWRfYnkiOiJhamlzZXRpYXdhbjg4M0BnbWFpbC5jb20iLCJpYXQiOjE3NzE4MjY4NzgsImV4cCI6MTgwMzM2Mjg3OH0.QGf_n8o61GxhYMFJL_fIyBP5xyDfnmG3nH9p6goPXgU"
        )
            .setEnableDebugLogs(true)
            .build()

        TapakAsih.initialize(this, config)
    }
}
```

## Langkah 2: Update AndroidManifest.xml

Update file `app/src/main/AndroidManifest.xml` di app Anda:

**SEBELUM:**

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <!-- Internet permission for API calls -->
    <uses-permission android:name="android.permission.INTERNET" />

    <!-- Network state permission for offline detection -->
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

</manifest>
```

**SESUDAH:**

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <!-- Internet permission for API calls -->
    <uses-permission android:name="android.permission.INTERNET" />

    <!-- Network state permission for offline detection -->
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <application
        android:name=".MyApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.EMoneyChecker">
        <!-- Activity declarations di sini -->
    </application>

</manifest>
```

**PENTING:** Tambahkan `android:name=".MyApplication"` di tag `<application>`!

## Langkah 3: Update MainActivity.kt

Update `app/src/main/java/com/paondev/emoneychecker/MainActivity.kt`:

**HAPUS baris-baris ini dari onCreate():**

```kotlin
// ❌ HAPUS inisialisasi SDK dari sini:
val config = TapakAsihConfig.Builder("eyJhbGci...")
    .setEnableDebugLogs(true)
    .build()

TapakAsih.initialize(application, config)
```

**HAPUS baris ini dari setContent block:**

```kotlin
// ❌ HAPUS dari Composable block:
if (TapakAsih.needsSessionId()) {
    TapakAsih.showSessionDialog()
}
```

**TAMBAHKAN ini di setContent block (sebagai gantinya):**

```kotlin
setContent {
    EMoneyCheckerTheme {
        val navController = rememberNavController()

        // ✅ TAMBAHKAN INI:
        LaunchedEffect(Unit) {
            delay(300) // delay 300ms untuk memastikan Activity siap

            if (TapakAsih.needsSessionId()) {
                TapakAsih.showSessionDialog()
            }
        }
        // ------------------------

        val context = LocalContext.current
        val dsManager = remember { DataStoreManager(context) }

        val viewModel: SettingsViewModel = viewModel(
            factory = SettingsViewModelFactory(dsManager)
        )

        LaunchedEffect(cardDataState.value) {
            if (cardDataState.value != null) {
                delay(7_000)
                cardDataState.value = null
            }
        }

        NavHost(
            navController = navController,
            startDestination = SplashDestination.route
        ) {
            composable(SplashDestination.route) {
                SplashScreen()

                LaunchedEffect(cardDataState.value) {
                    delay(1000)
                    navController.navigate(HomeDestination.route) {
                        popUpTo(SplashDestination.route) { inclusive = true }
                    }
                }
            }
            navRegistration(
                navController = navController,
                viewModel = viewModel,
                cardDataState = cardDataState
            )
        }
    }
}
```

## Ringkasan Perubahan

### File: MyApplication.kt (BARU)

- [x] Buat file baru
- [x] Inisialisasi SDK di Application class
- [x] Tidak ada crash karena initialization di tempat yang benar

### File: AndroidManifest.xml

- [x] Tambahkan `android:name=".MyApplication"` di `<application>` tag

### File: MainActivity.kt

- [x] Hapus inisialisasi SDK dari onCreate()
- [x] Hapus showSessionDialog() langsung dari Composable block
- [x] Tambahkan LaunchedEffect dengan delay 300ms
- [x] Cek dan show dialog di dalam LaunchedEffect

## Verifikasi

Setelah melakukan perubahan:

1. **Clean Build**

   ```bash
   ./gradlew clean
   ```

2. **Build App**

   ```bash
   ./gradlew build
   ```

3. **Install dan Test**
   - Install app di device/emulator
   - App seharusnya TIDAK crash saat pertama dibuka
   - Dialog session ID seharusnya muncul setelah ~300ms
   - Setelah session ID dimasukkan, dialog tidak muncul lagi

## Troubleshooting

### Masalah: Masih Crash

**Solusi:**

- Pastikan `android:name=".MyApplication"` ada di manifest
- Pastikan file MyApplication.kt ada di lokasi yang benar
- Cek logcat untuk error lain

### Masalah: Dialog Tidak Muncul

**Solusi:**

- Pastikan delay di LaunchedEffect cukup (coba 500ms)
- Cek logcat: `TapakAsih.isInitialized()`
- Pastikan session ID belum disimpan sebelumnya

### Masalah: Build Error

**Solusi:**

- Sync Gradle
- Clean build
- Pastikan semua import benar

## Perbedaan Penting

### SEBELUM (Crash):

```kotlin
// Inisialisasi di Activity
onCreate() {
    TapakAsih.initialize(application, config) // ❌

    setContent {
        if (TapakAsih.needsSessionId()) {
            TapakAsih.showSessionDialog() // ❌ Langsung di Composable
        }
    }
}
```

### SESUDAH (Aman):

```kotlin
// Inisialisasi di Application class (MyApplication.kt)
class MyApplication : Application() {
    onCreate() {
        TapakAsih.initialize(this, config) // ✅ Aman
    }
}

// Hanya cek dan show dialog di Activity dengan delay
onCreate() {
    setContent {
        LaunchedEffect(Unit) {
            delay(300) // ✅ Delay untuk Activity siap
            if (TapakAsih.needsSessionId()) {
                TapakAsih.showSessionDialog() // ✅ Di dalam LaunchedEffect
            }
        }
    }
}
```

## Catatan

- SDK sekarang lebih aman dan mengikuti best practice Android
- Dialog hanya muncul ketika Activity benar-benar siap
- Inisialisasi hanya sekali di Application class
- Tidak ada BadTokenException lagi
