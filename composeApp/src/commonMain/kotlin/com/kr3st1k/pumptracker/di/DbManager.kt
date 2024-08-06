package com.kr3st1k.pumptracker.di

import com.kr3st1k.pumptracker.core.db.dao.ScoresDao
import com.kr3st1k.pumptracker.core.db.AppDatabase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DbManager : KoinComponent {
    val db: AppDatabase by inject()

    fun getScoreDao(): ScoresDao {
        return db.scoresDao()
    }
}