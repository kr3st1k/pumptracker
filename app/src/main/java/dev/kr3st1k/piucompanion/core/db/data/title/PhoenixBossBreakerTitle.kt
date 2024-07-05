package dev.kr3st1k.piucompanion.core.db.data.title

import dev.kr3st1k.piucompanion.core.db.data.score.Score

class PhoenixBossBreakerTitle(
    name: String,
    val songName: String,
    val difficulty: String
) : PhoenixTitle(name, "Boss Breaker") {

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