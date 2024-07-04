package dev.kr3st1k.piucompanion.core.modules

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object LoginManagerModule {
    val loginModule = module {
        single {
            androidContext().getSharedPreferences("StuffPrefs", Context.MODE_PRIVATE)
        }
    }
}
