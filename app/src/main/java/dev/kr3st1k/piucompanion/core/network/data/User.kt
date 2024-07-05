package dev.kr3st1k.piucompanion.core.network.data

data class User(
    val username: String,
    val titleName: String,
    val backgroundUri: String,
    val avatarUri: String,
    val recentGameAccess: String,
    val coinValue: String,
    var pumbility: String? = null,
    val trueUser: Boolean = false
) {
    constructor() : this(
        "unavailable",
        "unavailable",
        "unavailable",
        "unavailable",
        "unavailable",
        "unavailable",
        "unavailable"
    )
}
//TODO remove constructor