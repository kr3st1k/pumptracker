package dev.kr3st1k.piucompanion.core.objects

data class NewsThumbnailObject (
    val name: String,
    val id: Int,
    val type: String,
    val link: String
 )

data class News (
 val id: Int,
 val text: Int,
 val type: String
)

data class NewsBanner (
    val id: Int,
    val pictureUrl: String,
    val uri: String
)