package dev.kr3st1k.piucompanion.core.network.data.score

data class BestUserScore(
    override val songName: String,
    override val difficulty: String,
    override val score: String,
    override val rank: String,
) : Score(songName, null, difficulty, score, rank) {
    constructor() : this(
        "No scores",
        "No scores",
        "000,000",
        "SSS+"
    )

    constructor(error: String) : this(
        error,
        error,
        "000,000",
        "SSS+"
    )
}
