package dev.kr3st1k.piucompanion.core.network

import dev.kr3st1k.piucompanion.core.helpers.Utils.checkIfLoginSuccess
import dev.kr3st1k.piucompanion.core.network.data.BestUserScore
import dev.kr3st1k.piucompanion.core.network.data.BgInfo
import dev.kr3st1k.piucompanion.core.network.data.LatestScore
import dev.kr3st1k.piucompanion.core.network.data.LoadableList
import dev.kr3st1k.piucompanion.core.network.data.News
import dev.kr3st1k.piucompanion.core.network.data.NewsBanner
import dev.kr3st1k.piucompanion.core.network.data.Pumbility
import dev.kr3st1k.piucompanion.core.network.data.ReleaseResponse
import dev.kr3st1k.piucompanion.core.network.data.User
import dev.kr3st1k.piucompanion.core.network.parsers.BestUserScoresParser
import dev.kr3st1k.piucompanion.core.network.parsers.LatestScoresParser
import dev.kr3st1k.piucompanion.core.network.parsers.NewsBannerParser
import dev.kr3st1k.piucompanion.core.network.parsers.NewsListParser
import dev.kr3st1k.piucompanion.core.network.parsers.PumbilityParser
import dev.kr3st1k.piucompanion.core.network.parsers.UserParser
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.Parameters
import io.ktor.http.URLProtocol
import io.ktor.http.parameters
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object NetworkRepositoryImpl : NetworkRepository {
    private const val BASEURL = "am-pass.net"
    private const val BASEPIUURL = "www.piugame.com"
    private const val UPDATEHOST = "kr3st1k.me"
    private const val PIUVERSIONURL = "piu/update.txt"
    private const val BGJSONURL = "piu/piu_bg_database.json"
    private const val LOGINPOSTPATH = "bbs/login_check.php"
    private val jsonWorker = Json { isLenient = true }
    private val client: HttpClient = KtorInstance.getHttpClient()
    override suspend fun getDocument(
        host: String,
        path: String,
        params: Parameters,
        checkLogin: Boolean,
    ): Document? {
        val request = client.get {
            url {
                protocol = URLProtocol.HTTPS
                this.host = host
                path(path)
                parameters.appendAll(params)
            }

        }
        val requestText: String = request.body()

        if (checkLogin && !checkIfLoginSuccess(requestText)) {
            if (!checkIfLoginSuccessRequest()) {
                return null
            }
            return getDocument(host, path, params, true)
        }

        return Jsoup.parse(requestText)
    }

    override suspend fun getGithubUpdateInfo(): ReleaseResponse {
        val cl = HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
        val req = cl.get("https://api.github.com/repos/kr3st1k/PIU_companion/releases/latest")
        val res: ReleaseResponse = req.body()

        return res
    }

    override suspend fun getUpdateInfo(): String {
        val response = client.get {
            url {
                protocol = URLProtocol.HTTPS
                host = UPDATEHOST
                path(PIUVERSIONURL)
            }
        }
        return response.body<String>()
    }

    override suspend fun getBgJson(): MutableList<BgInfo> {
        val response = client.get {
            url {
                protocol = URLProtocol.HTTPS
                host = UPDATEHOST
                path(BGJSONURL)
            }
        }
        val responseJsonText = response.body<String>()
        return jsonWorker.decodeFromString(responseJsonText)
    }

    override suspend fun checkIfLoginSuccessRequest(): Boolean {
        val t = client.get {
            url {
                protocol = URLProtocol.HTTPS
                host = BASEURL
                path("/")
            }
        }
        return checkIfLoginSuccess(t.body())
    }

    override suspend fun loginToAmPass(
        login: String,
        password: String,
        rememberMe: Boolean,
    ): Boolean {
        client.get("https://$BASEURL")
        client.submitForm(
            url = "https://$BASEURL/$LOGINPOSTPATH",
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
        val document = getDocument(BASEPIUURL, "phoenix_notice") ?: return mutableListOf()
        return NewsListParser.parse(document)
    }

    override suspend fun getPumbilityInfo(): Pumbility? {
        val document = getDocument(BASEPIUURL, "my_page/pumbility.php", checkLogin = true)
            ?: return null
        return PumbilityParser.parse(document)
    }

    override suspend fun getUserInfo(): User? {
        val document = getDocument(BASEPIUURL, "my_page/pumbility.php", checkLogin = true)
            ?: return null
        return UserParser.parse(document)
    }

    private suspend fun <T> reqAndAddAll(
        params: Parameters,
        resList: MutableList<T>,
        onRequest: suspend (url: Parameters) -> LoadableList<T>?,
    ): Boolean? {
        val result = onRequest(params) ?: return null
        resList.addAll(result.res)
        return result.isLoadMore
    }

    private suspend fun <T> buildRequestScorePagination(
        page: Int? = null,
        lvl: String = "",
        resList: MutableList<T>,
        onRequest: suspend (url: Parameters) -> LoadableList<T>?,
    ): LoadableList<T>? {
        var isLoadMore = true

        if (page == null) {
            for (i in 1..2) {
                if (!isLoadMore)
                    break
                val result = reqAndAddAll(Parameters.build {
                    append("lv", lvl)
                    append("page", i.toString())
                }, resList, onRequest) ?: return null
                isLoadMore = result
            }
        } else {
            val result = reqAndAddAll(Parameters.build {
                append("lv", lvl)
                append("page", page.toString())
            }, resList, onRequest) ?: return null
            isLoadMore = result
        }
        return LoadableList(resList, isLoadMore)
    }

    override suspend fun getBestUserScores(
        page: Int?,
        lvl: String,
        res: MutableList<BestUserScore>,
    ): LoadableList<BestUserScore>? {
        return buildRequestScorePagination(page, lvl, res) { parameters ->
            val document =
                getDocument(BASEPIUURL, "my_page/my_best_score.php", params = parameters, true)
                    ?: return@buildRequestScorePagination null
            return@buildRequestScorePagination BestUserScoresParser.parse(document)
        }
    }

    override suspend fun getLatestScores(length: Int): MutableList<LatestScore>? {
        val document =
            getDocument(BASEPIUURL, path = "my_page/recently_played.php", checkLogin = true)
            ?: return null
        return LatestScoresParser.parse(document)
    }

}