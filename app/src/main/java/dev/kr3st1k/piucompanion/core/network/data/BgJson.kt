package dev.kr3st1k.piucompanion.core.network.data

import kotlinx.serialization.Serializable

@Serializable
data class BgInfo (
    val jacket: String,
    val song_name: String
)