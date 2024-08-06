package com.kr3st1k.pumptracker.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kr3st1k.pumptracker.core.db.dao.ScoresDao
import com.kr3st1k.pumptracker.core.db.data.score.BestScore
import com.kr3st1k.pumptracker.core.db.data.score.LatestScore

@Database(entities = [LatestScore::class, BestScore::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scoresDao(): ScoresDao


}
