package dev.kr3st1k.piucompanion.objects

import kotlinx.serialization.Serializable

@Serializable
data class CookieData(
    val name: String,
    val value: String,
    val domain: String?,
    val path: String,
    val secure: Boolean,
    val httpOnly: Boolean,
    val expires: Long?,
)
