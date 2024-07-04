package dev.kr3st1k.piucompanion.di

import android.content.Context
import android.net.ConnectivityManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object InternetManagerModule {
    val internetModule = module {
        single {
            androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        }
    }
}
