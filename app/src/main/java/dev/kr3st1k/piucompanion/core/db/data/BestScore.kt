package dev.kr3st1k.piucompanion.core.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "best_scores")
data class BestScore(
    override val songName: String,
    override val difficulty: String,
    override val score: String,
    override val rank: String,
    @PrimaryKey @ColumnInfo(name = "hash") val hash: String,
) : Score(songName, null, difficulty, score, rank)