package dev.kr3st1k.piucompanion.core.db.data.title

import dev.kr3st1k.piucompanion.core.db.data.score.BestScore
import dev.kr3st1k.piucompanion.core.db.data.score.Score

class PhoenixDifficultyTitle(
    name: String,
    private val difficultyNumber: Int,
    private val ratingMax: Int
) : PhoenixTitle(name, PhoenixCategoryItem.Difficulty) {

    override fun completionProgress(scores: List<Score>): Float {
        if (scores.last() is BestScore) {
            var pumbility = 0
            scores.filter {
                it.difficulty.filter { it2 -> it2.isDigit() }
                    .map { it2 -> it2.toString().toInt() }.joinToString("")
                    .toInt() == difficultyNumber
            }.forEach {
                if (it is BestScore)
                    pumbility += it.pumbilityScore
            }
            return if (pumbility >= ratingMax)
                1F
            else
                pumbility.toFloat() / ratingMax
        }
        return 0F
    }

    override fun completionProgressValue(scores: List<Score>): String {
        if (scores.last() is BestScore) {
            var pumbility = 0
            scores.filter {
                it.difficulty.filter { it2 -> it2.isDigit() }
                    .map { it2 -> it2.toString().toInt() }.joinToString("")
                    .toInt() == difficultyNumber
            }.forEach {
                if (it is BestScore)
                    pumbility += it.pumbilityScore
            }
            return pumbility.toString()
        }
        return "0"
    }

}