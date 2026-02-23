# Cara Menggunakan TapakAsih SDK di Android Kotlin

## 1. Menambahkan Dependency ke build.gradle.kts

Tambahkan JitPack repository ke file `settings.gradle.kts` atau `build.gradle.kts` tingkat root:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

Tambahkan dependency di `build.gradle.kts` (module level):

```kotlin
dependencies {
    implementation("com.github.ajipaon:tapakasih:1.0.0")
}
```

## 2. Inisialisasi SDK di Application Class

Buat class yang extends `Application` dan inisialisasi SDK:

```kotlin
import android.app.Application
import com.paondev.lib.tapakasih.TapakAsih
import com.paondev.lib.tapakasih.config.TapakAsihConfig

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Inisialisasi TapakAsih SDK
        val config = TapakAsihConfig.Builder("YOUR_DEVELOPER_TOKEN")
            .setEnableDebugLogs(true) // Hanya untuk development
            .build()

        TapakAsih.initialize(this, config)
    }
}
```

Jangan lupa daftarkan Application class di `AndroidManifest.xml`:

```xml
<application
    android:name=".MyApplication"
    ... >
    ...
</application>
```

## 3. Mengelola Session ID

### Penting: Session ID Harus Di-set Sebelum Tracking

SDK memerlukan Session ID sebelum dapat melakukan tracking. Ada beberapa cara untuk mengaturnya:

#### Opsi 1: Cek dan Tampilkan Dialog (RECOMMENDED)

Panggil di Activity pertama yang dibuka:

```kotlin
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.paondev.lib.tapakasih.TapakAsih

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Cek apakah session ID diperlukan
        if (TapakAsih.needsSessionId()) {
            // Tampilkan dialog untuk user input session ID
            TapakAsih.showSessionDialog()
        }
    }
}
```

#### Opsi 2: Menggunakan Listener

Set listener di Application class untuk notifikasi otomatis:

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val config = TapakAsihConfig.Builder("YOUR_DEVELOPER_TOKEN")
            .setEnableDebugLogs(true)
            .build()

        TapakAsih.initialize(this, config)

        // Set listener untuk menangani kebutuhan session ID
        TapakAsih.setOnSessionRequiredListener(object : TapakAsih.OnSessionRequiredListener {
            override fun onSessionRequired() {
                // Di sini Anda bisa:
                // 1. Tampilkan dialog: TapakAsih.showSessionDialog()
                // 2. Kirim event ke Activity untuk menampilkan dialog custom
                // 3. Log untuk debugging
                Log.d("TapakAsih", "Session ID is required")
            }
        })
    }
}
```

#### Opsi 3: Set Session ID Langsung

Jika aplikasi sudah memiliki session ID (misal dari login):

```kotlin
// Set session ID secara manual
TapakAsih.setSessionId("user-session-id-123")

// Atau set hanya jika belum ada
val wasSet = TapakAsih.setSessionIdIfEmpty("default-session-id")
if (wasSet) {
    Log.d("TapakAsih", "Session ID was set successfully")
}
```

### Mendapatkan Session ID

```kotlin
val sessionId = TapakAsih.getSessionId()
Log.d("TapakAsih", "Current Session ID: $sessionId")
```

### Menghapus Session ID

```kotlin
// Hapus session ID (logout)
TapakAsih.clearSessionId()
```

## 4. Tracking Activity

### Tracking Page Activity

```kotlin
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.paondev.lib.tapakasih.TapakAsih

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Track page visit
        TapakAsih.trackPage("MainActivity")
    }

    override fun onResume() {
        super.onResume()
        // Track saat activity resume
        TapakAsih.trackPage("MainActivity")
    }
}
```

### Tracking di Fragment

```kotlin
class MyFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Track fragment view
        TapakAsih.trackPage("MyFragment")
    }
}
```

### Tracking Event Kustom

```kotlin
// Tracking button click
button.setOnClickListener {
    TapakAsih.trackPage("ButtonClick-Submit")
}

// Tracking screen navigation
navigateToProfile()
TapakAsih.trackPage("ProfileScreen")
```

## 5. Status dan Konfigurasi

### Cek Status SDK

```kotlin
// Cek apakah SDK sudah diinisialisasi
if (TapakAsih.isInitialized()) {
    Log.d("TapakAsih", "SDK is initialized")
}

// Cek apakah session ID diperlukan
if (TapakAsih.needsSessionId()) {
    // Handle session requirement
}

// Mendapatkan konfigurasi
val config = TapakAsih.getConfig()
Log.d("TapakAsih", "Debug logs enabled: ${config.isEnableDebugLogs()}")
```

## 6. Penanganan Error

SDK otomatis menangani error dan mencatat log. Pastikan debug logs diaktifkan saat development:

```kotlin
val config = TapakAsihConfig.Builder("YOUR_DEVELOPER_TOKEN")
    .setEnableDebugLogs(BuildConfig.DEBUG) // Hanya debug mode
    .build()
```

Error yang mungkin terjadi:

- **Session ID tidak ada**: Log akan menampilkan "No session ID, cannot track"
- **Token expired**: Log akan menampilkan "Developer token is expired, cannot track"
- **Tracking disabled by server**: Log akan menampilkan "Tracking is disabled by server"

## 7. Contoh Implementasi Lengkap

### Aplikasi Sederhana dengan Tracking

```kotlin
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.paondev.lib.tapakasih.TapakAsih
import com.paondev.lib.tapakasih.config.TapakAsihConfig

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val config = TapakAsihConfig.Builder("YOUR_DEVELOPER_TOKEN")
            .setEnableDebugLogs(BuildConfig.DEBUG)
            .build()

        TapakAsih.initialize(this, config)
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Cek dan tampilkan dialog jika session ID diperlukan
        if (TapakAsih.needsSessionId()) {
            TapakAsih.showSessionDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        // Track page setiap kali activity resume
        TapakAsih.trackPage("MainActivity")
    }
}
```

### Aplikasi dengan Login dan Session ID dari Backend

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val config = TapakAsihConfig.Builder("YOUR_DEVELOPER_TOKEN")
            .setEnableDebugLogs(BuildConfig.DEBUG)
            .build()

        TapakAsih.initialize(this, config)

        // Cek apakah user sudah login dan punya session ID
        val userSessionId = getUserSessionIdFromPreferences()
        if (userSessionId != null) {
            TapakAsih.setSessionIdIfEmpty(userSessionId)
        }
    }

    private fun getUserSessionIdFromPreferences(): String? {
        // Ambil session ID dari SharedPreferences atau storage lain
        val prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        return prefs.getString("session_id", null)
    }
}

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener {
            performLogin { sessionId ->
                // Login berhasil, simpan session ID
                saveSessionId(sessionId)
                // Set ke TapakAsih SDK
                TapakAsih.setSessionId(sessionId)
                // Lanjut ke main activity
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }

    private fun performLogin(callback: (String) -> Unit) {
        // Implementasi login ke backend
        // Setelah berhasil, kembalikan session ID
        callback("user-session-123")
    }

    private fun saveSessionId(sessionId: String) {
        val prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        prefs.edit().putString("session_id", sessionId).apply()
    }
}
```

## 8. Best Practices

### 1. Inisialisasi Sekali Saja

Inisialisasi SDK hanya sekali di `Application.onCreate()`, jangan inisialisasi berulang kali.

### 2. Handle Session ID dengan Benar

- Selalu cek `TapakAsih.needsSessionId()` sebelum tracking
- Tampilkan dialog session ID di Activity pertama yang user lihat
- Set session ID dari login jika aplikasi memiliki sistem auth

### 3. Track di Waktu yang Tepat

- Track page di `onResume()` untuk mendeteksi setiap kunjungan
- Gunakan nama page yang jelas dan konsisten
- Track event penting (button click, navigation, dll)

### 4. Debug Mode Hanya di Development

Matikan debug mode di production:

```kotlin
val isDebug = BuildConfig.DEBUG
val config = TapakAsihConfig.Builder("YOUR_DEVELOPER_TOKEN")
    .setEnableDebugLogs(isDebug)
    .build()
```

### 5. Privacy dan Security

- Jangan hardcode developer token di production
- Gunakan environment variables atau secure storage
- Ikuti praktik terbaik untuk handling user data
- Session ID bisa berisi informasi sensitif, handle dengan hati-hati

## 9. Troubleshooting

### Masalah: App crash saat inisialisasi

**Solusi**: Pastikan Anda tidak memanggil `TapakAsih.showSessionDialog()` di Application class. Panggil di Activity saja.

### Masalah: Tracking tidak berfungsi

**Solusi**: Pastikan:

1. Session ID sudah di-set
2. Developer token valid dan tidak expired
3. Server tidak mengirim status `NO_DEMAND`

### Masalah: Dialog tidak muncul

**Solusi**: Pastikan:

1. Anda memanggil `TapakAsih.showSessionDialog()` di Activity (bukan Fragment atau Application)
2. Activity sudah dalam state onResume() atau onCreate()

## Support

Untuk pertanyaan dan bantuan, kunjungi:

- GitHub: https://github.com/ajipaon/tapakasih
- Email: support@paondev.com
