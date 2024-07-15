package dev.kr3st1k.piucompanion.core.db.repository

import dev.kr3st1k.piucompanion.core.db.dao.ScoresDao
import dev.kr3st1k.piucompanion.core.db.data.score.BestScore
import dev.kr3st1k.piucompanion.core.db.data.score.LatestScore
import dev.kr3st1k.piucompanion.core.db.data.score.Score
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ScoresRepository(private val scoresDao: ScoresDao) {
    suspend fun insertScore(score: Score) {
        if (score is BestScore)
            withContext(Dispatchers.IO) {
                scoresDao.insertBest(score)
            }
        if (score is LatestScore)
            withContext(Dispatchers.IO) {
                withContext(Dispatchers.IO) {
                    scoresDao.insertLatest(score)
                }
            }
    }

    suspend fun insertBestScores(scores: List<BestScore>) =
        withContext(Dispatchers.IO) {
            scores.forEach { scoresDao.insertBest(it) }
        }


    suspend fun insertLatestScores(scores: List<LatestScore>) =
        withContext(Dispatchers.IO) {
            scores.forEach { scoresDao.insertLatest(it) }
        }


    suspend fun getLatestScores(): List<LatestScore> =
        withContext(Dispatchers.IO) { scoresDao.getAllLatestScores() }


    suspend fun getBestScores(): List<BestScore> =
        withContext(Dispatchers.IO) { scoresDao.getAllBestScores() }

    suspend fun deleteScores() =
        withContext(Dispatchers.IO) {
            scoresDao.deleteAllLatest()
            scoresDao.deleteAllBest()
        }

}