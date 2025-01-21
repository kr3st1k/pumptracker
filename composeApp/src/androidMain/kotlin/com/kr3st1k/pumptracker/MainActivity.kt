package com.kr3st1k.pumptracker

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.retainedComponent
import com.kr3st1k.pumptracker.nav.RootComponent
import okio.Path
import okio.Path.Companion.toOkioPath


class MainActivity : ComponentActivity() {
    private lateinit var component: RootComponent

    @SuppressLint("HardwareIds")
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        getOkioPath = {
            this.filesDir.toOkioPath()
        }
        DeviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        component = retainedComponent {
            RootComponent(it)
        }
        openBrowser = { site ->
            val customTabsIntent = CustomTabsIntent.Builder().build()
            customTabsIntent.launchUrl(this, Uri.parse(site))
        }
        val isUpdateRequired = mutableStateOf(false)
        setContent {
            val windowClass = calculateWindowSizeClass()
            val showNavigationRail =
                windowClass.widthSizeClass != WindowWidthSizeClass.Compact
            
            val view = LocalView.current
            
            val isSystemDark = isSystemInDarkTheme()
            updateFrame = {
                val window = (view.context as Activity).window
                window.statusBarColor = Color.Transparent.toArgb()
                window.navigationBarColor = Color.Transparent.toArgb()
                
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    window.isStatusBarContrastEnforced = false
                    window.isNavigationBarContrastEnforced = false
                }
                
                WindowCompat.setDecorFitsSystemWindows(window, false)
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !(if(isSystemDefault.value) isSystemDark else isDarkTheme.value)
            }
            SideEffect {
                updateFrame()
            }
            
            App(
                root = component,
                isUpdateRequired = isUpdateRequired,
                seedColor = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
                    dynamicDarkColorScheme(this).primary
                } else {
                    Color.Blue
                },
                showNavigationRail = showNavigationRail,
                updateFrameFromAndroid = updateFrame
            )
        }
    }

    companion object {
        lateinit var DeviceId: String
        lateinit var openBrowser: (site: String) -> Unit
        lateinit var getOkioPath: () -> Path
        
        lateinit var updateFrame: () -> Unit
    }
}