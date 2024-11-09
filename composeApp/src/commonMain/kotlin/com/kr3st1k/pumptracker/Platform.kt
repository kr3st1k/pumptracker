package com.kr3st1k.pumptracker

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.ktor.client.*
import okio.Path

interface Platform {
    val type: String
    val name: String
}

expect fun getPlatform(): Platform

expect fun getUserDirectory(): Path

expect fun getPlatformHttpClient(): HttpClient

expect fun getNakedHttpClient(): HttpClient

expect fun encryptAES(data: String): String

expect fun decryptAES(data: String): String

expect fun openSiteInBrowser(site: String)

@Composable
expect fun ScrollBarForDesktop(modifier: Modifier, listState: LazyGridState)

@Composable
expect fun ScrollBarForDesktop(modifier: Modifier, listState: LazyListState)

expect fun getDownloadsFolder(): Path

fun getDownloadExtension(platform: Platform): String {
    val name = platform.name.lowercase()

    return if (name.contains("win"))
        ".exe"
    else if (name.contains("mac") || name.contains("darwin"))
        ".dmg"
    else if (name.contains("android"))
        ".apk"
    else
        ".zip"
}

expect fun openFile(file: Path)

fun getPlatformForJKS(platform: Platform): String {
    val name = platform.name.lowercase()

    return if (name.contains("android"))
        "android"
    else if (name.contains("win") || name.contains("mac") || name.contains("darwin"))
        "pc"
    else
        "unknown"
}