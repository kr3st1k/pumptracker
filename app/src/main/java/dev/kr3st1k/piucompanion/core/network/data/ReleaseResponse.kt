package dev.kr3st1k.piucompanion.core.network.data

import kotlinx.serialization.Serializable

@Serializable
data class ReleaseResponse(
    val url: String,
    val assets_url: String,
    val upload_url: String,
    val html_url: String,
    val id: Int,
    val author: ResponseAuthor,
    val node_id: String,
    val tag_name: String,
    val target_commitish: String,
    val name: String,
    val draft: Boolean,
    val prerelease: Boolean,
    val created_at: String,
    val published_at: String,
    val assets: List<ResponseAsset>,
    val tarball_url: String,
    val zipball_url: String,
    val body: String
)

@Serializable
data class ResponseAuthor(
    val login: String,
    val id: Int,
    val node_id: String,
    val avatar_url: String,
    val gravatar_id: String,
    val url: String,
    val html_url: String,
    val followers_url: String,
    val following_url: String,
    val gists_url: String,
    val starred_url: String,
    val subscriptions_url: String,
    val organizations_url: String,
    val repos_url: String,
    val events_url: String,
    val received_events_url: String,
    val type: String,
    val site_admin: Boolean
)

@Serializable
data class ResponseAsset(
    val url: String,
    val id: Int,
    val node_id: String,
    val name: String,
    val label: String?,
    val uploader: Uploader,
    val content_type: String,
    val state: String,
    val size: Int,
    val download_count: Int,
    val created_at: String,
    val updated_at: String,
    val browser_download_url: String
)

@Serializable
data class Uploader(
    val login: String,
    val id: Int,
    val node_id: String,
    val avatar_url: String,
    val gravatar_id: String,
    val url: String,
    val html_url: String,
    val followers_url: String,
    val following_url: String,
    val gists_url: String,
    val starred_url: String,
    val subscriptions_url: String,
    val organizations_url: String,
    val repos_url: String,
    val events_url: String,
    val received_events_url: String,
    val type: String,
    val site_admin: Boolean
)

