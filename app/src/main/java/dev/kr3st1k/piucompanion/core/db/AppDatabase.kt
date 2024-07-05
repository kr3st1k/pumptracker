package dev.kr3st1k.piucompanion.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.kr3st1k.piucompanion.core.db.dao.ScoresDao
import dev.kr3st1k.piucompanion.core.db.data.score.BestScore
import dev.kr3st1k.piucompanion.core.db.data.score.LatestScore

@Database(entities = [LatestScore::class, BestScore::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scoresDao(): ScoresDao


}
