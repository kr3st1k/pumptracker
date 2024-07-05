package dev.kr3st1k.piucompanion.core.network.data.title

import dev.kr3st1k.piucompanion.core.db.data.title.PhoenixTitle

data class TitleItem(
    val name: String,
    val isAchieved: Boolean = false,
    val isSelected: Boolean = false,
    val description: String,
    val value: String = "",
    var titleInfo: PhoenixTitle? = null,
    var progress: Float? = null,
    var progressValue: String? = null
)
