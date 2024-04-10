package dev.kr3st1k.piucompanion.core.prefs

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object BgManagerModule {
    val bgModule = module {
        single { BgManager() }
        factory { androidContext().filesDir }
    }
}