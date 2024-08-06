package com.kr3st1k.pumptracker.core.db.data.title

import com.kr3st1k.pumptracker.core.db.data.score.BestScore
import com.kr3st1k.pumptracker.core.db.data.score.Score
import com.kr3st1k.pumptracker.core.helpers.Utils
import kotlin.math.ceil

class PhoenixCoopTitle(
    name: String,
    private val ratingMax: Int
) : PhoenixTitle(name, PhoenixCategoryItem.Coop) {

    override fun completionProgress(scores: List<Score>): Float {
        if (scores.last() is BestScore) {
            var pumbility = 0
            scores.filter {
                it.difficulty[0] == 'C'
            }.forEach {
                if (it is BestScore)
                    pumbility += ceil(2000 * Utils.pointMultiplier(it.score)).toInt()
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
                it.difficulty[0] == 'C'
            }.forEach {
                if (it is BestScore)
                    pumbility += ceil(2000 * Utils.pointMultiplier(it.score)).toInt()
            }
            return pumbility.toString()
        }
        return "0"
    }

}