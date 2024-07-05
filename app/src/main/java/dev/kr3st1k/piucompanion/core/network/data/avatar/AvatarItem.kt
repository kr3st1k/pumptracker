package dev.kr3st1k.piucompanion.core.network.data.avatar

data class AvatarItem(
    val name: String,
    val isBought: Boolean = false,
    val isSelected: Boolean = false,
    val price: String,
    val avatarUrl: String,
    val value: String = ""
)
