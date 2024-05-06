package dev.kr3st1k.piucompanion.core.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "best_scores")
data class BestScore(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "song_name") val songName: String,
    @ColumnInfo(name = "song_background_uri") val songBackgroundUri: String,
    @ColumnInfo(name = "difficulty") val difficulty: String,
    @ColumnInfo(name = "score") val score: String,
    @ColumnInfo(name = "rank") val rank: String,
    @ColumnInfo(name = "hash") val hash: String?,
)