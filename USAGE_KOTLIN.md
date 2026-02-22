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
        val config = TapakAsihConfig.Builder()
            .setApiKey("YOUR_API_KEY")
            .setEndpoint("https://api.tapakasih.com")
            .setSessionTimeout(30 * 60 * 1000) // 30 menit dalam milidetik
            .setDebugEnabled(true) // Hanya untuk development
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

## 3. Menggunakan Activity Tracker

### Memulai Tracking Activity

```kotlin
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.paondev.lib.tapakasih.TapakAsih
import com.paondev.lib.tapakasih.network.ActivityRequest

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Mulai tracking activity ini
        val activityRequest = ActivityRequest(
            activityName = "MainActivity",
            userId = "user_123",
            sessionId = TapakAsih.getInstance().getCurrentSessionId(),
            metadata = mapOf(
                "screen" to "home",
                "timestamp" to System.currentTimeMillis()
            )
        )

        TapakAsih.getInstance().trackActivity(activityRequest)
    }
}
```

### Tracking Event Kustom

```kotlin
// Tracking event kustom
val eventRequest = ActivityRequest(
    activityName = "ButtonClick",
    userId = "user_123",
    sessionId = TapakAsih.getInstance().getCurrentSessionId(),
    metadata = mapOf(
        "button_id" to "submit_button",
        "form_name" to "registration"
    )
)

TapakAsih.getInstance().trackActivity(eventRequest)
```

## 4. Mengelola Session

SDK secara otomatis mengelola session, tapi Anda bisa juga mengontrolnya secara manual:

```kotlin
// Mendapatkan session ID saat ini
val currentSessionId = TapakAsih.getInstance().getCurrentSessionId()

// Mengakhiri session secara manual
TapakAsih.getInstance().endSession()

// Memulai session baru
TapakAsih.getInstance().startNewSession()
```

## 5. Mengelola Token

SDK menyimpan token otomatis, tapi Anda bisa mengelola token secara manual:

```kotlin
// Mendapatkan token yang tersimpan
val token = TapakAsih.getInstance().getTokenManager().getToken()

// Mengatur token baru
TapakAsih.getInstance().getTokenManager().setToken("new_token_here")

// Menghapus token (logout)
TapakAsih.getInstance().getTokenManager().clearToken()
```

## 6. Penanganan Error dan Callback

Gunakan callback untuk menangani hasil tracking:

```kotlin
import com.paondev.lib.tapakasih.network.ActivityCheckResponse

TapakAsih.getInstance().trackActivity(activityRequest, object : TapakAsih.Callback {
    override fun onSuccess(response: ActivityCheckResponse) {
        // Tracking berhasil
        Log.d("TapakAsih", "Activity tracked successfully: ${response.message}")
    }

    override fun onError(error: Exception) {
        // Terjadi error
        Log.e("TapakAsih", "Failed to track activity", error)
    }
})
```

## 7. Menggunakan di Fragment

```kotlin
class MyFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activityRequest = ActivityRequest(
            activityName = "MyFragment",
            userId = "user_123",
            sessionId = TapakAsih.getInstance().getCurrentSessionId(),
            metadata = mapOf("fragment" to "details")
        )

        TapakAsih.getInstance().trackActivity(activityRequest)
    }
}
```

## 8. Konfigurasi Advanced

### Custom Network Timeout

```kotlin
val config = TapakAsihConfig.Builder()
    .setApiKey("YOUR_API_KEY")
    .setEndpoint("https://api.tapakasih.com")
    .setNetworkTimeout(10 * 1000) // 10 detik
    .setRetryCount(3)
    .build()
```

### Offline Mode

SDK akan otomatis menyimpan event ketika offline dan mengirimnya ketika online:

```kotlin
val config = TapakAsihConfig.Builder()
    .setApiKey("YOUR_API_KEY")
    .setEndpoint("https://api.tapakasih.com")
    .setMaxOfflineEvents(100) // Maksimal 100 event disimpan offline
    .build()
```

## 9. Best Practices

### 1. Inisialisasi Sekali Saja

Inisialisasi SDK hanya sekali di `Application.onCreate()`, jangan inisialisasi berulang kali.

### 2. Gunakan Session ID yang Konsisten

Gunakan `TapakAsih.getInstance().getCurrentSessionId()` untuk mendapatkan session ID yang konsisten.

### 3. Handle Error dengan Benar

Selalu gunakan callback untuk menangani error dan memberikan feedback ke user.

### 4. Debug Mode Hanya di Development

Matikan debug mode di production:

```kotlin
val isDebug = BuildConfig.DEBUG
val config = TapakAsihConfig.Builder()
    .setApiKey("YOUR_API_KEY")
    .setEndpoint("https://api.tapakasih.com")
    .setDebugEnabled(isDebug)
    .build()
```

### 5. Privacy dan Security

- Jangan hardcode API key di production
- Gunakan secure storage untuk menyimpan sensitive data
- Ikuti praktik terbaik untuk handling user data

## 10. Contoh Implementasi Lengkap

Lihat file `ExampleUsage.kt` untuk contoh implementasi lengkap.

## Support

Untuk pertanyaan dan bantuan, kunjungi:

- GitHub: https://github.com/ajipaon/tapakasih
- Email: support@paondev.com
