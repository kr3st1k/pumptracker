package dev.kr3st1k.piucompanion.core.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "latest_scores")
data class LatestScore(
    @PrimaryKey(autoGenerate = true) val id: Int,
    override val songName: String,
    override val songBackgroundUri: String,
    override val difficulty: String,
    override val score: String,
    override val rank: String,
    @ColumnInfo(name = "datetime") val datetime: String, //ToDo need to be at UTC+0
    @ColumnInfo(name = "hash") val hash: String,
) : Score(songName, songBackgroundUri, difficulty, score, rank)