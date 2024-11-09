package com.kr3st1k.pumptracker

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.network.CacheStrategy
import coil3.network.NetworkFetcher
import coil3.network.ktor.asNetworkClient
import com.kr3st1k.pumptracker.core.network.KtorInstance.getHttpClient
import com.kr3st1k.pumptracker.di.BgManager
import com.kr3st1k.pumptracker.di.LoginManager
import com.kr3st1k.pumptracker.nav.RootComponent
import com.kr3st1k.pumptracker.nav.RootComponentImpl
import com.kr3st1k.pumptracker.ui.components.dialogs.UpdateModal
import com.kr3st1k.pumptracker.ui.theme.PIUCompanionTheme
import kotlinx.coroutines.runBlocking
import okio.Path

var isDynamicColors = mutableStateOf(LoginManager().getIsDynamicColor())
var isDarkTheme = mutableStateOf(LoginManager().getIsDarkTheme())
var isSystemDefault = mutableStateOf(LoginManager().getIsSystemDefault())

var updateFrame = {}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun App(
    root: RootComponent,
    seedColor: Color = Color.Unspecified,
    showNavigationRail: Boolean = true,
    isUpdateRequired: MutableState<Boolean> = mutableStateOf(false),
    updateLink: MutableState<String> = mutableStateOf(""),
    updateFrameFromAndroid: () -> Unit = {}
) {
    runBlocking {
        BgManager().saveJKS()
    }
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
                    RootComponentImpl(
                        root,
                        showNavigationRail
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@JvmName("factory")
fun KtorNetworkFetcherFactory() = NetworkFetcher.Factory(
    networkClient = { getHttpClient().asNetworkClient() },
    cacheStrategy = { CacheStrategy() },
)

fun newImageLoader(ctx: PlatformContext, dir: Path): ImageLoader {
    return ImageLoader.Builder(ctx)
        .components {
            add(
                factory = KtorNetworkFetcherFactory()
            )
        }
        .memoryCache {
            MemoryCache.Builder()
                .maxSizePercent(ctx, 0.20)
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