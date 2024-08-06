package com.kr3st1k.pumptracker

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Base64
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kr3st1k.pumptracker.MainActivity
import com.kr3st1k.pumptracker.MainActivity.Companion.ctx
import com.kr3st1k.pumptracker.core.db.AppDatabase
import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import okio.Path
import okio.Path.Companion.toOkioPath
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AndroidPlatform : Platform {
    override val type: String = "Mobile"
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun getUserDirectory(): Path {
    return ctx.filesDir.toOkioPath()
}

fun createDatabaseBuilder(ctx: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath("db.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    ).fallbackToDestructiveMigration(true)
}

actual fun getPlatformHttpClient(): HttpClientEngineFactory<HttpClientEngineConfig> {
    return OkHttp
}

actual fun encryptAES(data: String): String {
    val key = SecretKeySpec(MainActivity.DeviceId.toByteArray(), "AES")
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    val iv = IvParameterSpec(ByteArray(16)) // replace with your IV if you have one
    cipher.init(Cipher.ENCRYPT_MODE, key, iv)
    val res = cipher.doFinal(data.toByteArray())
    return Base64.encodeToString(res, Base64.URL_SAFE)
}

actual fun decryptAES(data: String): String {
    val encrypted = Base64.decode(data, Base64.URL_SAFE)
    val key = SecretKeySpec(MainActivity.DeviceId.toByteArray(), "AES")
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    val iv = IvParameterSpec(ByteArray(16)) // replace with your IV if you have one
    cipher.init(Cipher.DECRYPT_MODE, key, iv)
    return String(cipher.doFinal(encrypted))
}

actual fun openSiteInBrowser(site: String) {
    val customTabsIntent = CustomTabsIntent.Builder().build()
    customTabsIntent.launchUrl(ctx, Uri.parse(site))
}

@Composable
actual fun ScrollBarForDesktop(modifier: Modifier, listState: LazyGridState) {

}
@Composable
actual fun ScrollBarForDesktop(modifier: Modifier, listState: LazyListState) {

}

actual fun getDownloadsFolder(): Path =
    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toOkioPath()

actual fun openFile(file: Path) {
}