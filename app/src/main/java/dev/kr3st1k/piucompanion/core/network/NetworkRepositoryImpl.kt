package dev.kr3st1k.piucompanion.core.network

import dev.kr3st1k.piucompanion.core.helpers.Utils.checkIfLoginSuccess
import dev.kr3st1k.piucompanion.core.network.data.BestUserScore
import dev.kr3st1k.piucompanion.core.network.data.BgInfo
import dev.kr3st1k.piucompanion.core.network.data.LatestScore
import dev.kr3st1k.piucompanion.core.network.data.LoadableList
import dev.kr3st1k.piucompanion.core.network.data.News
import dev.kr3st1k.piucompanion.core.network.data.NewsBanner
import dev.kr3st1k.piucompanion.core.network.data.User
import dev.kr3st1k.piucompanion.core.network.parsers.BestUserScoresParser
import dev.kr3st1k.piucompanion.core.network.parsers.LatestScoresParser
import dev.kr3st1k.piucompanion.core.network.parsers.NewsBannerParser
import dev.kr3st1k.piucompanion.core.network.parsers.NewsListParser
import dev.kr3st1k.piucompanion.core.network.parsers.UserParser
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.Parameters
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.parameters
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object NetworkRepositoryImpl : NetworkRepository {
    private const val BASEURL = "https://am-pass.net"
    private const val BASEPIUURL = "https://www.piugame.com"
    private const val PIUVERSIONURL = "https://kr3st1k.me/piu/update.txt"
    private const val BGJSONURL = "https://kr3st1k.me/piu/piu_bg_database.json"
    private const val LOGINPOSTPATH = "/bbs/login_check.php"
    private val jsonWorker = Json { isLenient = true }
    private val client: HttpClient = KtorInstance.getHttpClient()
    override suspend fun getDocument(
        url: String,
        checkLogin: Boolean,
    ): Document? {
        val request = client.get(url)
        val requestText: String = request.body()

        if (checkLogin && !checkIfLoginSuccess(requestText)) {
            if (!checkIfLoginSuccessRequest()) {
                return null
            }
            return getDocument(url)
        }

        return Jsoup.parse(requestText)
    }

    override suspend fun getUpdateInfo(): String {
        val response = client.get(PIUVERSIONURL)
        return response.body<String>()
    }

    override suspend fun getBgJson(): MutableList<BgInfo> {
        val response = client.get(BGJSONURL)
        val responseJsonText = response.body<String>()
        return jsonWorker.decodeFromString(responseJsonText)
    }

    override suspend fun checkIfLoginSuccessRequest(): Boolean {
        val t = client.get(BASEURL)
        return checkIfLoginSuccess(t.body())
    }

    override suspend fun loginToAmPass(
        login: String,
        password: String,
        rememberMe: Boolean,
    ): Boolean {
        client.submitForm(
            url = "$BASEURL$LOGINPOSTPATH",
            formParameters = parameters {
                append("url", "/")
                append("mb_id", login)
                append("mb_password", password)
                if (rememberMe)
                    append("auto_login", "on")
            }
        )
        return checkIfLoginSuccessRequest()
    }

    override suspend fun getNewsBanners(): MutableList<NewsBanner> {
        val document = getDocument(BASEPIUURL) ?: return mutableListOf()
        return NewsBannerParser.parse(document)
    }

    override suspend fun getNewsList(): MutableList<News> {
        val document = getDocument("$BASEPIUURL/phoenix_notice") ?: return mutableListOf()
        return NewsListParser.parse(document)
    }

    override suspend fun getUserInfo(): User? {
        val document = getDocument("$BASEPIUURL/my_page/play_data.php", true)
            ?: return null
        return UserParser.parse(document)
    }

    private suspend fun <T> buildRequestScorePagination(
        page: Int? = null,
        lvl: String = "",
        path: String,
        resList: MutableList<T>,
        onRequest: suspend (url: URLBuilder) -> LoadableList<T>?,
    ): LoadableList<T>? {
        var isLoadMore = true

        val url = URLBuilder(
            protocol = URLProtocol.HTTPS,
            host = BASEPIUURL,
            pathSegments = path.split("/"),
            parameters = Parameters.build {
                append("lv", lvl)
                append("page", "")
            }
        )
        if (page == null) {
            for (i in 1..2) {
                if (!isLoadMore)
                    break
                url.parameters["page"] = i.toString()
                val result = onRequest(url) ?: return null
                resList.addAll(result.res)
                isLoadMore = result.isLoadMore
            }
        } else {
            url.parameters["page"] = page.toString()
            val result = onRequest(url) ?: return null
            resList.addAll(result.res)
            isLoadMore = result.isLoadMore
        }
        return LoadableList(resList, isLoadMore)
    }

    override suspend fun getBestUserScores(
        page: Int?,
        lvl: String,
        res: MutableList<BestUserScore>,
    ): LoadableList<BestUserScore>? {
        return buildRequestScorePagination(page, lvl, "my_page/my_best_score.php", res) { url ->
            val document =
                getDocument(url.buildString(), true) ?: return@buildRequestScorePagination null
            return@buildRequestScorePagination BestUserScoresParser.parse(document)
        }
    }

    override suspend fun getLatestScores(length: Int): MutableList<LatestScore>? {
        val document = getDocument("$BASEPIUURL/my_page/recently_played.php", true)
            ?: return null
        return LatestScoresParser.parse(document)
    }

}