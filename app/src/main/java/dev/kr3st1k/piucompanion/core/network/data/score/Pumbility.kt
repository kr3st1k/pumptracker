package dev.kr3st1k.piucompanion.core.network.data.score

import dev.kr3st1k.piucompanion.core.network.data.User

data class Pumbility(
    val user: User?,
    val scores: List<PumbilityScore>
)
