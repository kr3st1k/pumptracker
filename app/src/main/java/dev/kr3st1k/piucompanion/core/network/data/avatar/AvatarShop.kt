package dev.kr3st1k.piucompanion.core.network.data.avatar

import dev.kr3st1k.piucompanion.core.network.data.User

data class AvatarShop(
    val user: User?,
    val items: List<AvatarItem>
)
