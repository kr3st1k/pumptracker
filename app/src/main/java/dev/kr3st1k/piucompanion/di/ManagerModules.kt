package dev.kr3st1k.piucompanion.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import dev.kr3st1k.piucompanion.core.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object ManagerModules {
    val bgModule = module {
        single { BgManager() }
        factory { androidContext().filesDir }
    }

    val dbModule = module {
        single {
            Room.databaseBuilder(
                androidContext(),
                AppDatabase::class.java, "db"
            ).fallbackToDestructiveMigration().build()
        }
    }

    val internetModule = module {
        single {
            androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        }
    }

    val loginModule = module {
        single {
            androidContext().getSharedPreferences("StuffPrefs", Context.MODE_PRIVATE)
        }
    }
}