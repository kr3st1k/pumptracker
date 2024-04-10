package dev.kr3st1k.piucompanion.core.prefs

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.io.FileInputStream
import java.io.FileOutputStream

object BgManagerModule {
    val bgModule = module {
        single { BgManager() }
        factory { (name: String) -> FileInputStream(name) }
        factory { (name: String) -> FileOutputStream(name) }
        factory { androidContext().filesDir }
    }
}