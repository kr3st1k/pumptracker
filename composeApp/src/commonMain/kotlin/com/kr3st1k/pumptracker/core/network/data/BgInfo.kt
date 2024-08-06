package com.kr3st1k.pumptracker.core.network.data

import kotlinx.serialization.Serializable

@Serializable
data class BgInfo (
    val jacket: String,
    val song_name: String
)