package dev.kr3st1k.piucompanion.core.network.data.title

import dev.kr3st1k.piucompanion.core.network.data.User

data class TitleShop(
    val user: User?,
    val titles: List<TitleItem>
)
