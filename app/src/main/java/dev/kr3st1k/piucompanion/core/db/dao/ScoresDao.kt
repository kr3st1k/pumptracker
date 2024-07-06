package dev.kr3st1k.piucompanion.core.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.kr3st1k.piucompanion.core.db.data.score.BestScore
import dev.kr3st1k.piucompanion.core.db.data.score.LatestScore

@Dao
interface ScoresDao {
    @Query("SELECT * FROM best_scores")
    fun getAllBestScores(): List<BestScore>

    @Query("SELECT * FROM latest_scores")
    fun getAllLatestScores(): List<LatestScore>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLatest(score: LatestScore)

    @Delete
    suspend fun deleteLatest(score: LatestScore)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBest(score: BestScore)

    @Delete
    suspend fun deleteBest(score: BestScore)

    @Query("DELETE FROM best_scores")
    fun deleteAllBest()

    @Query("DELETE FROM latest_scores")
    fun deleteAllLatest()
}