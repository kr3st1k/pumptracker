package dev.kr3st1k.piucompanion.core.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pumbility_scores")
data class PumbilityScore(
    override val songName: String,
    override var songBackgroundUri: String?,
    override val difficulty: String,
    override val score: String,
    override val rank: String,
    @PrimaryKey @ColumnInfo(name = "hash") val hash: String,
    @ColumnInfo(name = "datetime") val datetime: String,
) : Score(songName, songBackgroundUri, difficulty, score, rank)

