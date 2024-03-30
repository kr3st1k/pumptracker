package dev.kr3st1k.piucompanion

import android.app.Application
import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import dev.kr3st1k.piucompanion.screens.Navigation
import dev.kr3st1k.piucompanion.ui.theme.PIUCompanionTheme

class MainActivity : ComponentActivity() {
    companion object {
        lateinit var userAgent: String
            private set
        lateinit var secChUa: String
            private set
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val webView = WebView(this)
        userAgent = webView.settings.userAgentString
        val secChUaPattern = Regex("""(Chromium|Chrome)\/(\d+)\.(\d+)\.(\d+)\.(\d+)""")

        secChUa = buildString {
            val chromeMatch = secChUaPattern.find(userAgent)
            if (chromeMatch != null) {
                append("\"Chromium\";v=\"${chromeMatch.groupValues[2]}\"")
            }
            if (chromeMatch != null) {
                append(", \"Android WebView\";v=\"${chromeMatch.groupValues[2]}\"")
            }
            append(", \"Not-A.Brand\";v=\"99\"")
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            PIUCompanionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }
}

class PIUCompanion : Application(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.20)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(filesDir.resolve("image_cache"))
                    .maxSizeBytes(250 * 1024 * 1024)
                    .build()
            }
            .build()
    }
}