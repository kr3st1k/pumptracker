package dev.kr3st1k.piucompanion.core.network

import dev.kr3st1k.piucompanion.core.network.data.BestUserScore
import dev.kr3st1k.piucompanion.core.network.data.BgInfo
import dev.kr3st1k.piucompanion.core.network.data.LatestScore
import dev.kr3st1k.piucompanion.core.network.data.LoadableList
import dev.kr3st1k.piucompanion.core.network.data.News
import dev.kr3st1k.piucompanion.core.network.data.NewsBanner
import dev.kr3st1k.piucompanion.core.network.data.User
import io.ktor.http.Parameters
import org.jsoup.nodes.Document

interface NetworkRepository {
    suspend fun getDocument(
        host: String,
        path: String = "/",
        params: Parameters = Parameters.Empty,
        checkLogin: Boolean = false,
    ): Document?
    suspend fun getUpdateInfo(): String
    suspend fun getBgJson(): MutableList<BgInfo>
    suspend fun checkIfLoginSuccessRequest(): Boolean
    suspend fun loginToAmPass(login: String, password: String, rememberMe: Boolean = true): Boolean
    suspend fun getNewsBanners(): MutableList<NewsBanner>
    suspend fun getNewsList(): MutableList<News>
    suspend fun getUserInfo(): User?
    suspend fun getBestUserScores(
        page: Int? = null,
        lvl: String = "",
        res: MutableList<BestUserScore> = mutableListOf(),
    ): LoadableList<BestUserScore>?

    suspend fun getLatestScores(length: Int): MutableList<LatestScore>?
}
