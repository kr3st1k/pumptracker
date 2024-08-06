package com.kr3st1k.pumptracker.core.db.data.title

import com.kr3st1k.pumptracker.core.db.data.score.Score

class PhoenixBossBreakerTitle(
    name: String,
    val songName: String,
    val difficulty: String
) : PhoenixTitle(name, PhoenixCategoryItem.BossBreaker) {

    override fun completionProgress(scores: List<Score>): Float {
        for (score in scores) {
            if (songName == score.songName && difficulty == score.difficulty) {
                return 1F
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