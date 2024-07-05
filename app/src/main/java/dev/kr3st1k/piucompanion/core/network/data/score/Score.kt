package dev.kr3st1k.piucompanion.core.network.data.score

abstract class Score(
    open val songName: String,
    open val songBackgroundUri: String?,
    open val difficulty: String,
    open val score: String,
    open val rank: String,
)