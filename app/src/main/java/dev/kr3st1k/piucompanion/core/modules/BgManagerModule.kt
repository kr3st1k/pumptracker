package dev.kr3st1k.piucompanion.core.modules

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object BgManagerModule {
    val bgModule = module {
        single { BgManager() }
        factory { androidContext().filesDir }
    }
}