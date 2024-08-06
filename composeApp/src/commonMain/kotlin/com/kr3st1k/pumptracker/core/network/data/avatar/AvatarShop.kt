package com.kr3st1k.pumptracker.core.network.data.avatar

import com.kr3st1k.pumptracker.core.network.data.User

data class AvatarShop(
    val user: User?,
    val items: List<AvatarItem>
)
