package com.kr3st1k.pumptracker.core.network.data.score

import com.kr3st1k.pumptracker.core.network.data.User

data class Pumbility(
    val user: User?,
    val scores: List<PumbilityScore>
)
