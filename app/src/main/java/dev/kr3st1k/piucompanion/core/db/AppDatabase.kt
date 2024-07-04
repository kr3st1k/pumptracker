package dev.kr3st1k.piucompanion.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.kr3st1k.piucompanion.core.db.dao.ScoresDao
import dev.kr3st1k.piucompanion.core.db.data.BestScore
import dev.kr3st1k.piucompanion.core.db.data.LatestScore
import dev.kr3st1k.piucompanion.core.db.data.PumbilityScore

@Database(entities = [LatestScore::class, BestScore::class, PumbilityScore::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scoresDao(): ScoresDao


}
