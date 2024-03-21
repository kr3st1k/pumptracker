package dev.kr3st1k.piucompanion.objects

data class LatestScore (
    val songName: String,
    val songBackgroundUri: String,
    val difficulty: String,
    val score: String,
    val rank: String,
    val datetime: String //TODO No String. make a Data
)

//Spray D19 880,910 Broken A+
//2024-03-16 18:21:38 (GMT+9)