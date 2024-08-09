package com.kr3st1k.pumptracker

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kr3st1k.pumptracker.core.db.AppDatabase
import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import okio.Path
import okio.Path.Companion.toPath
import sn
import java.awt.Desktop
import java.io.File
import java.io.IOException
import java.net.URI
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.system.exitProcess


class JVMPlatform: Platform {
    override val type: String = "Desktop"
    override val name: String = System.getProperty("os.name")
}

actual fun getPlatform(): Platform = JVMPlatform()

fun createDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbDir = getUserDirectory()
    val dbFile = File(dbDir.toFile(), "db.db")
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath,
    ).fallbackToDestructiveMigration(true)
}

actual fun getPlatformHttpClient(): HttpClientEngineFactory<HttpClientEngineConfig> {
    return OkHttp
}

actual fun getUserDirectory(): Path {
    val os = System.getProperty("os.name").lowercase()
    val dbDir = when {
        os.contains("win") -> System.getenv("AppData") + "/PumpTracker"
        os.contains("mac") -> System.getProperty("user.home") + "/Library/Application Support/PumpTracker"
        else -> System.getProperty("user.home") + "/.local/share/PumpTracker"
    }
    return dbDir.toPath()
}

@OptIn(ExperimentalEncodingApi::class)
actual fun encryptAES(data: String): String {
    val key = SecretKeySpec(sn!!.toByteArray(), "AES")
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    val iv = IvParameterSpec(ByteArray(16)) // replace with your IV if you have one
    cipher.init(Cipher.ENCRYPT_MODE, key, iv)
    val res = cipher.doFinal(data.toByteArray())
    return Base64.encode(res)
}

@OptIn(ExperimentalEncodingApi::class)
actual fun decryptAES(data: String): String {
    val encrypted = Base64.decode(data)
    val key = SecretKeySpec(sn!!.toByteArray(), "AES")
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    val iv = IvParameterSpec(ByteArray(16)) // replace with your IV if you have one
    cipher.init(Cipher.DECRYPT_MODE, key, iv)
    return String(cipher.doFinal(encrypted))
}

actual fun openSiteInBrowser(site: String) {
    if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
        Desktop.getDesktop().browse(URI(site))
    }
}

@Composable
actual fun ScrollBarForDesktop(modifier: Modifier, listState: LazyGridState) {
    val styleScroll = ScrollbarStyle(
        minimalHeight = LocalScrollbarStyle.current.minimalHeight,
        thickness = 6.dp,
        shape = LocalScrollbarStyle.current.shape,
        hoverDurationMillis = LocalScrollbarStyle.current.hoverDurationMillis,
        unhoverColor = MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.6F),
        hoverColor = MaterialTheme.colorScheme.inverseSurface
    )
    VerticalScrollbar(
        modifier = modifier,
        style = styleScroll,
        adapter = rememberScrollbarAdapter(listState)
    )

}

@Composable
actual fun ScrollBarForDesktop(modifier: Modifier, listState: LazyListState) {
    val styleScroll = ScrollbarStyle(
        minimalHeight = LocalScrollbarStyle.current.minimalHeight,
        thickness = 6.dp,
        shape = LocalScrollbarStyle.current.shape,
        hoverDurationMillis = LocalScrollbarStyle.current.hoverDurationMillis,
        unhoverColor = MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.6F),
        hoverColor = MaterialTheme.colorScheme.inverseSurface
    )
    VerticalScrollbar(
        modifier = modifier,
        style = styleScroll,
        adapter = rememberScrollbarAdapter(listState)
    )

}

actual fun getDownloadsFolder(): Path =
    getUserDirectory()

actual fun openFile(file: Path) {
    val platform = getPlatform()
    val platformName = platform.name.lowercase()

    val runtime = Runtime.getRuntime()
    var process: Process? = null
    try {
        process = runtime.exec(arrayOf(file.toString()))
        exitProcess(0)
    } catch (e: IOException) {
        throw RuntimeException(e)
    }

}