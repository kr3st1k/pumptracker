package dev.kr3st1k.piucompanion.core.network.data

data class BestUserScore(
    val songName: String,
    val songBackgroundUri: String,
    val difficulty: String,
    val score: String,
    val rank: String,
) {
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
