package dev.kr3st1k.piucompanion.core.modules

import androidx.room.Room
import dev.kr3st1k.piucompanion.core.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object DbManagerModule {
    val dbModule = module {
        single {
            Room.databaseBuilder(
                androidContext(),
                AppDatabase::class.java, "db"
            ).build()
        }
    }
}
