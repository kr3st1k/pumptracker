package dev.kr3st1k.piucompanion.core.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.kr3st1k.piucompanion.core.db.data.BestScore
import dev.kr3st1k.piucompanion.core.db.data.LatestScore
import dev.kr3st1k.piucompanion.core.db.data.PumbilityScore

@Dao
interface ScoresDao {
    @Query("SELECT * FROM best_scores")
    fun getAllBestScores(): List<BestScore>

    @Query("SELECT * FROM latest_scores")
    fun getAllLatestScores(): List<LatestScore>

    @Query("SELECT * FROM pumbility_scores")
    fun getAllPumbilityScores(): List<PumbilityScore>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLatest(score: LatestScore)

    @Delete
    suspend fun deleteLatest(score: LatestScore)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBest(score: BestScore)

    @Delete
    suspend fun deleteBest(score: BestScore)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPumbility(score: PumbilityScore)

    @Delete
    suspend fun deletePumbility(score: PumbilityScore)

    @Query("DELETE FROM best_scores")
    fun deleteAllBest()

    @Query("DELETE FROM latest_scores")
    fun deleteAllLatest()

    @Query("DELETE FROM pumbility_scores")
    fun deleteAllPumbility()
}