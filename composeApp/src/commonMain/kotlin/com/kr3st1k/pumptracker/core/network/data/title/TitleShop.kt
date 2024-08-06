package com.kr3st1k.pumptracker.core.network.data.title

import com.kr3st1k.pumptracker.core.network.data.User

data class TitleShop(
    val user: User?,
    val titles: List<TitleItem>
)
