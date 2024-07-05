package dev.kr3st1k.piucompanion.core.network.data.title

data class TitleItem(
    val name: String,
    val isAchieved: Boolean = false,
    val isSelected: Boolean = false,
    val description: String,
    val value: String = ""
)
