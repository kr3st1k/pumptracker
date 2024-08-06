package com.kr3st1k.pumptracker

import android.app.Application
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.kr3st1k.pumptracker.core.db.AppDatabase
import com.kr3st1k.pumptracker.di.ManagerModules
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                ManagerModules.loginModule,
                ManagerModules.bgModule,
                ManagerModules.internetModule,
                module {
                    single<AppDatabase>{createDatabaseBuilder(this@App)
                        .setDriver(BundledSQLiteDriver())
                        .setQueryCoroutineContext(Dispatchers.IO)
                        .build()
                    }
                }
            )
        }
    }
}