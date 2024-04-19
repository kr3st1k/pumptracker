package dev.kr3st1k.piucompanion.core.db.data

import androidx.room.ColumnInfo

abstract class Score(
    @ColumnInfo(name = "song_name") open val songName: String,
    @ColumnInfo(name = "song_background_uri") open val songBackgroundUri: String,
    @ColumnInfo(name = "difficulty") open val difficulty: String,
    @ColumnInfo(name = "score") open val score: String,
    @ColumnInfo(name = "rank") open val rank: String,
)

