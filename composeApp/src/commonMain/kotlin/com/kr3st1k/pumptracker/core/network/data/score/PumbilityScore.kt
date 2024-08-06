package com.kr3st1k.pumptracker.core.network.data.score

data class PumbilityScore(
    override val songName: String,
    override val songBackgroundUri: String,
    override val difficulty: String,
    override val score: String,
    override val rank: String,
    val datetime: String,
) : Score(songName, songBackgroundUri, difficulty, score, rank)
