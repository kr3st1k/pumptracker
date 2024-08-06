package com.kr3st1k.pumptracker.core.network

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import com.kr3st1k.pumptracker.core.helpers.Utils.checkIfLoginSuccess
import com.kr3st1k.pumptracker.core.network.data.BgInfo
import com.kr3st1k.pumptracker.core.network.data.LoadableList
import com.kr3st1k.pumptracker.core.network.data.ReleaseResponse
import com.kr3st1k.pumptracker.core.network.data.User
import com.kr3st1k.pumptracker.core.network.data.avatar.AvatarShop
import com.kr3st1k.pumptracker.core.network.data.news.News
import com.kr3st1k.pumptracker.core.network.data.news.NewsBanner
import com.kr3st1k.pumptracker.core.network.data.score.BestUserScore
import com.kr3st1k.pumptracker.core.network.data.score.LatestScore
import com.kr3st1k.pumptracker.core.network.data.score.Pumbility
import com.kr3st1k.pumptracker.core.network.data.title.TitleShop
import com.kr3st1k.pumptracker.core.network.parsers.AvatarShopParser
import com.kr3st1k.pumptracker.core.network.parsers.BestUserScoresParser
import com.kr3st1k.pumptracker.core.network.parsers.LatestScoresParser
import com.kr3st1k.pumptracker.core.network.parsers.NewsBannerParser
import com.kr3st1k.pumptracker.core.network.parsers.NewsListParser
import com.kr3st1k.pumptracker.core.network.parsers.PumbilityParser
import com.kr3st1k.pumptracker.core.network.parsers.TitleShopParser
import com.kr3st1k.pumptracker.core.network.parsers.UserParser
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.*
//import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path
import okio.buffer
import okio.use
import java.io.FileOutputStream

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

        return Ksoup.parse(requestText)
    }

    override suspend fun formPost(
        host: String,
        path: String,
        params: Parameters,
        checkLogin: Boolean
    ): Boolean? {

        val request = client.submitForm(
            url = "https://$host/$path",
            formParameters = params
        )

        val requestText: String = request.body()

        if (checkLogin && !checkIfLoginSuccess(requestText)) {
            if (!checkIfLoginSuccessRequest()) {
                return null
            }
            return formPost(host, path, params, true)
        }

        return request.status.value == 200
    }

    override suspend fun getGithubUpdateInfo(): ReleaseResponse {
        val cl = HttpClient() {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
        val req = cl.get("https://api.github.com/repos/kr3st1k/PIU_companion/releases/latest")
        val res: ReleaseResponse = req.body()

        return res
    }

    suspend fun downloadUpdate(url: String, destination: Path, onProgress: (Long, Long) -> Unit) {
        val outputStream = FileSystem.SYSTEM.sink(destination).buffer()
        var bytesSize = 0L
        client.prepareGet(url).execute { httpResponse ->
            val channel: ByteReadChannel = httpResponse.bodyAsChannel()
            while (!channel.isClosedForRead) {
                val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
                while (!packet.isEmpty) {
                    val bytes = packet.readBytes()
                    outputStream.write(bytes)
                    bytesSize += bytes.size
                    if (bytesSize == httpResponse.contentLength()!!)
                        outputStream.close()
                    onProgress(bytesSize, httpResponse.contentLength()!!)
                }
            }
        }
//        val response: HttpResponse = client.get(url) {
//            onDownload { bytesSentTotal, contentLength ->
//                onProgress(bytesSentTotal,contentLength)
//            }
//        }
//
//        val responseBody: ByteArray = response.body()
//

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

    override suspend fun logout() {
        client.get {
            url {
                protocol = URLProtocol.HTTPS
                host = BASEURL
                path("/bbs/logout.php")
            }
        }
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

    override suspend fun getAvatarShopInfo(): AvatarShop? {
        val document = getDocument(BASEPIUURL, "my_page/avatar_shop.php") ?: return null
        return AvatarShopParser.parse(document)
    }

    override suspend fun getTitleShopInfo(): TitleShop? {
        val document = getDocument(BASEPIUURL, "my_page/title.php") ?: return null
        return TitleShopParser.parse(document)
    }

    override suspend fun setTitle(value: String): Boolean? {
        return formPost(
            BASEPIUURL,
            "logic/user_title_update.php",
            parameters {
                append("no", value)
            })
    }

    override suspend fun setAvatar(value: String): Boolean? {
        return formPost(
            BASEPIUURL,
            "logic/user_avatar_update.php",
            parameters {
                append("no", value)
            })
    }

    override suspend fun buyAvatar(value: String): Boolean? {
        return formPost(
            BASEPIUURL,
            "ajax/user_avatar_buy.php",
            parameters {
                append("no", value)
            }
        )
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
        val document = getDocument(BASEPIUURL, "my_page/play_data.php", checkLogin = true)
            ?: return null
        return UserParser.parse(document)
    }

    private suspend fun <T> reqAndAddAll(
        params: Parameters,
        resList: MutableList<T>,
        onRequest: suspend (url: Parameters) -> LoadableList<T>?,
    ): Pair<Boolean, Int>? {
        val result = onRequest(params) ?: return null
        resList.addAll(result.res)
        return Pair(result.isLoadMore, result.lastPageNumber)
    }

    private suspend fun <T> buildRequestScorePagination(
        page: Int? = null,
        lvl: String = "",
        resList: MutableList<T>,
        onRequest: suspend (url: Parameters) -> LoadableList<T>?,
    ): LoadableList<T>? {
        var isLoadMore = true
        var pageCount = 1
        if (page == null) {
            for (i in 1..2) {
                if (!isLoadMore)
                    break
                val result = reqAndAddAll(Parameters.build {
                    append("lv", lvl)
                    append("page", i.toString())
                }, resList, onRequest) ?: return null
                isLoadMore = result.first
                pageCount = result.second
            }
        } else {
            val result = reqAndAddAll(Parameters.build {
                append("lv", lvl)
                append("page", page.toString())
            }, resList, onRequest) ?: return null
            isLoadMore = result.first
            pageCount = result.second
        }
        return LoadableList(resList, isLoadMore, pageCount)
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