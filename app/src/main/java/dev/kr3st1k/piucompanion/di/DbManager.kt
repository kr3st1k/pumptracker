package dev.kr3st1k.piucompanion.di

import dev.kr3st1k.piucompanion.core.db.AppDatabase
import dev.kr3st1k.piucompanion.core.db.dao.ScoresDao
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DbManager : KoinComponent {
    val db: AppDatabase by inject()

    fun getScoreDao(): ScoresDao {
        return db.scoresDao()
    }
}