package dev.kr3st1k.piucompanion.core.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.kr3st1k.piucompanion.core.db.data.BestScore
import dev.kr3st1k.piucompanion.core.db.data.LatestScore

@Dao
interface LatestScoresDao {
    @Query("SELECT * FROM best_scores")
    fun getAllBestScores(): MutableList<BestScore>

    @Query("SELECT * FROM latest_scores")
    fun getAllLatestScores(): MutableList<LatestScore>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(score: LatestScore)

    @Delete
    suspend fun delete(score: LatestScore)

}