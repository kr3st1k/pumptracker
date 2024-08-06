package com.kr3st1k.pumptracker.core.db.data.title

import com.kr3st1k.pumptracker.core.db.data.score.Score

class PhoenixSkillTitle(
    name: String,
    val songName: String,
    val difficulty: String
) : PhoenixTitle(name, PhoenixCategoryItem.Skill) {

    override fun completionProgress(scores: List<Score>): Float {
        for (score in scores) {
            if (songName == score.songName && difficulty == score.difficulty) {
                val res = score.score.replace(",", "").toInt()
                return if (res >= 990000)
                    1F
                else
                    res.toFloat() / 990000
            }
        }
        return 0F
    }

    override fun completionProgressValue(scores: List<Score>): String {
        for (score in scores) {
            if (songName == score.songName && difficulty == score.difficulty) {
                return score.score
            }
        }
        return "0"
    }

}