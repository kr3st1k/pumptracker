package dev.kr3st1k.piucompanion.core.network.data

data class LatestScore(
    val songName: String,
    val songBackgroundUri: String,
    val difficulty: String,
    val score: String,
    val rank: String,
    val datetime: String,
)
