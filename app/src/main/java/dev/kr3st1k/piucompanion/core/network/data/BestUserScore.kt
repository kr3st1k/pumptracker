package dev.kr3st1k.piucompanion.core.network.data

data class BestUserScore(
    override val songName: String,
    override val songBackgroundUri: String,
    override val difficulty: String,
    override val score: String,
    override val rank: String,
) : Score(songName, songBackgroundUri, difficulty, score, rank) {
    constructor() : this(
        "No scores",
        "https://www.piugame.com/l_img/bg1.png",
        "No scores",
        "000,000",
        "SSS+"
    )

    constructor(error: String) : this(
        error,
        "https://www.piugame.com/l_img/bg1.png",
        error,
        "000,000",
        "SSS+"
    )
}
