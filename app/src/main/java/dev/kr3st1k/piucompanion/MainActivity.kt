package dev.kr3st1k.piucompanion

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import dev.kr3st1k.piucompanion.core.db.AppDatabase
import dev.kr3st1k.piucompanion.core.helpers.DownloadApk
import dev.kr3st1k.piucompanion.core.network.NetworkRepositoryImpl
import dev.kr3st1k.piucompanion.di.LoginManager
import dev.kr3st1k.piucompanion.ui.pages.HomeScreen
import dev.kr3st1k.piucompanion.ui.theme.PIUCompanionTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    companion object {
        lateinit var userAgent: String
            private set
        lateinit var db: AppDatabase
            private set
        lateinit var version: String
            private set
        var isOffline: Boolean = false

    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        val webView = WebView(this)

        enableEdgeToEdge()

        val loginManager = LoginManager()

        userAgent = webView.settings.userAgentString

        val packageInfo = baseContext.packageManager.getPackageInfo("com.kr3st1k.pumptracker", 0)
        version = "${packageInfo.versionName} (${packageInfo.longVersionCode})"

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            var isDynamicColors by remember { mutableStateOf(loginManager.getIsDynamicColor()) }
            var isDarkTheme by remember { mutableStateOf(loginManager.getIsDarkTheme()) }
            var isSystemDefault by remember { mutableStateOf(loginManager.getIsSystemDefault()) }
            DisposableEffect(Unit) {
                val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                    if (key == "dynamic_color")
                        isDynamicColors = loginManager.getIsDynamicColor()
                    if (key == "dark_theme")
                        isDarkTheme = loginManager.getIsDarkTheme()
                    if (key == "system_default")
                        isSystemDefault = loginManager.getIsSystemDefault()
                }
                loginManager.sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

                onDispose {
                    loginManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(
                        listener
                    )
                }
            }
            PIUCompanionTheme(
                darkTheme = if (isSystemDefault) isSystemInDarkTheme() else isDarkTheme,
                dynamicColor = isDynamicColors
            ) {
                var mustUpdate by remember { mutableStateOf(false) }
                var uri by remember { mutableStateOf("") }
                val windowClass = calculateWindowSizeClass(activity = this)
                val showNavigationRail =
                    windowClass.widthSizeClass != WindowWidthSizeClass.Compact
                val r = BuildConfig.BUILD_TYPE

                lifecycleScope.launch {
                    if (r != "debug") {
                        val update = NetworkRepositoryImpl.getGithubUpdateInfo()
                        if (update.name != version) {
                            mustUpdate = true
                            uri = update.assets[0].browser_download_url
                        }
                    }
                }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (mustUpdate && r != "debug")
                        AlertDialog(
                            onDismissRequest = {

                            },
                            title = { Text(text = "You must update the app") },
                            text = {
                                Text(text = "Update the app lol")
                            },
                            confirmButton = {
                                TextButton(onClick = {
                                    mustUpdate = false
                                    val downloadApk = DownloadApk(this@MainActivity)

                                    if (uri != "") {
                                        downloadApk.startDownloadingApk(uri)
                                    }
                                }) {
                                    Text(text = "Update")
                                }
                            }
                        )
                    else
                        HomeScreen(
                            showNavigationRail
                        )
                }
            }
        }
    }
}

