package com.kr3st1k.pumptracker

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import com.kr3st1k.pumptracker.di.LoginManager
import com.kr3st1k.pumptracker.ui.components.dialogs.UpdateModal

import com.kr3st1k.pumptracker.ui.pages.HomeScreen
import com.kr3st1k.pumptracker.ui.theme.PIUCompanionTheme
import kotlinx.coroutines.launch
import okio.Path

var isDynamicColors = mutableStateOf(LoginManager().getIsDynamicColor())
var isDarkTheme = mutableStateOf(LoginManager().getIsDarkTheme())
var isSystemDefault = mutableStateOf(LoginManager().getIsSystemDefault())

var updateFrame = {}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun App(
    seedColor: Color = Color.Unspecified,
    showNavigationRail: Boolean = true,
    isUpdateRequired: MutableState<Boolean> = mutableStateOf(false),
    updateLink: MutableState<String> = mutableStateOf(""),
    updateFrameFromAndroid: () -> Unit = {}
) {
    setSingletonImageLoaderFactory { context ->
        newImageLoader(context, getUserDirectory().resolve("image_cache"))
    }

    updateFrame = updateFrameFromAndroid
    PIUCompanionTheme(
        darkTheme = if (isSystemDefault.value) isSystemInDarkTheme() else isDarkTheme.value,
        seedColor = if (seedColor == Color.Unspecified || !isDynamicColors.value) Color.Blue else seedColor
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                if (isUpdateRequired.value) {
                    UpdateModal(
                        updateLink = updateLink,
                        onSkipClick = {
                            isUpdateRequired.value = false
                        },
                        changelog = "*Nice\n*Cock\nBro"
                    )
                } else {
                    HomeScreen(
                        showNavigationRail
                    )
                }
            }
        }
    }
}
fun newImageLoader(ctx: PlatformContext, dir: Path): ImageLoader {
    return ImageLoader.Builder(ctx)
        .memoryCache {
            MemoryCache.Builder()
                .maxSizePercent(ctx,0.20)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(dir)
                .maxSizeBytes(250 * 1024 * 1024)
                .build()
        }
        .build()
}