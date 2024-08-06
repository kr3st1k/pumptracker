package com.kr3st1k.pumptracker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat


class MainActivity : ComponentActivity() {
    @SuppressLint("HardwareIds")
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        val isUpdateRequired = mutableStateOf(false)
        setContent {
            ctx = LocalContext.current
            DeviceId = Settings.Secure.getString(ctx.contentResolver, Settings.Secure.ANDROID_ID)

            BackHandler(enabled = true) {

            }

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
        lateinit var ctx: Context
        
        lateinit var updateFrame: () -> Unit
    }
}