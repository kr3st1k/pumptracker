package com.kr3st1k.pumptracker.core.db.data.score

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
abstract class Score(
    @ColumnInfo(name = "song_name") open val songName: String,
    @ColumnInfo(name = "song_background_uri") open var songBackgroundUri: String?,
    @ColumnInfo(name = "difficulty") open val difficulty: String,
    @ColumnInfo(name = "score") open val score: String,
    @ColumnInfo(name = "rank") open val rank: String,
)
