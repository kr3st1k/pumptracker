package dev.kr3st1k.piucompanion

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import dev.kr3st1k.piucompanion.core.db.AppDatabase
import dev.kr3st1k.piucompanion.ui.screens.HomeScreen
import dev.kr3st1k.piucompanion.ui.theme.PIUCompanionTheme

class MainActivity : ComponentActivity() {
    companion object {
        lateinit var userAgent: String
            private set
        lateinit var db: AppDatabase
            private set
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val webView = WebView(this)

        userAgent = webView.settings.userAgentString
        db = AppDatabase.getInstance(this)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            PIUCompanionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}

