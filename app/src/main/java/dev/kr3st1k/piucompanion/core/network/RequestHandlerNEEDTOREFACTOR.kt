package dev.kr3st1k.piucompanion.core.network

import dev.kr3st1k.piucompanion.core.helpers.Utils
import dev.kr3st1k.piucompanion.core.network.data.BestUserScore
import dev.kr3st1k.piucompanion.core.network.data.BgInfo
import dev.kr3st1k.piucompanion.core.network.data.LatestScore
import dev.kr3st1k.piucompanion.core.network.data.News
import dev.kr3st1k.piucompanion.core.network.data.NewsBanner
import dev.kr3st1k.piucompanion.core.network.data.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.parameters
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.Locale

object RequestHandler {
    private const val BASE_URL = "https://am-pass.net"
    private const val BASE_PIU_URL = "https://www.piugame.com"
    private const val UPDATE_INFO_URL = "https://kr3st1k.me/piu/update.txt"
    private const val BG_JSON_URL = "https://kr3st1k.me/piu/piu_bg_database.json"
    private const val LOGOUT_STRING = "bbs/logout.php"
    private const val LOGIN_CHECK_URL = "$BASE_URL/bbs/login_check.php"
    private const val AUTO_LOGIN_PARAM = "auto_login"

    private val client: HttpClient = KtorInstance.getHttpClient()

    suspend fun getUpdateInfo(): String {
        val response = client.get(UPDATE_INFO_URL)
        return response.body<String>()
    }

    suspend fun getBgJson(): MutableList<BgInfo> {
        val response = client.get(BG_JSON_URL)
        val res = response.body<String>()
        val JSON = Json { isLenient = true }
        val list = JSON.decodeFromString(res) as MutableList<BgInfo>
        return list
    }

    private suspend fun checkIfLoginSuccessRequest(): Boolean {
        val t = client.get(BASE_URL)
        return checkIfLoginSuccess(t.body())
    }

    private fun checkIfLoginSuccess(data: String): Boolean = data.indexOf(LOGOUT_STRING) > 0

    suspend fun loginToAmPass(
        login: String,
        password: String,
        rememberMe: Boolean = true,
    ): Boolean {
        getDocument(client, BASE_URL)

        client.submitForm(
            url = LOGIN_CHECK_URL,
            formParameters = parameters {
                append("url", "/")
                append("mb_id", login)
                append("mb_password", password)
                if (rememberMe)
                    append(AUTO_LOGIN_PARAM, "on")
            }
        )
        return checkIfLoginSuccessRequest()
    }

    private fun getBackgroundImg(element: Element, addDomain: Boolean = true): String {
        val style = element.attr("style")
        return (if (addDomain) "https://www.piugame.com" else "") + style.substringAfter("background-image:url('")
            .substringBefore("')")
    }

    private suspend fun getDocument(
        client: HttpClient,
        uri: String,
        checkLogin: Boolean = false,
    ): Document? {
        val req = client.get(uri)
        val reqBody: String = req.body()

        if (checkLogin && !checkIfLoginSuccess(reqBody)) {
            if (!checkIfLoginSuccessRequest()) {
                return null
            }
            return getDocument(client, uri, true)
        }

        return Jsoup.parse(reqBody)
    }

    suspend fun getNewsBanners(): MutableList<NewsBanner> {

        val t = getDocument(client, "https://www.piugame.com") ?: return mutableListOf()

        val uniqueElements = t.select("a.img.resize.bgfix")
            .map { it to it.attr("href") }
            .distinctBy { it.second }
            .map { it.first }

        return uniqueElements.map { element ->
            NewsBanner(
                Utils.getWrId(element.attr("href"))?.toInt() ?: 0,
                getBackgroundImg(element),
                element.attr("href")
            )
        }.toMutableList()

    }

    suspend fun getNewsList(): MutableList<News> {
        val t =
            getDocument(client, "https://www.piugame.com/phoenix_notice") ?: return mutableListOf()

        val table = t.select("tbody")
        val trElements = table.select("tr")

        val res = mutableListOf<News>()

        for (elem in trElements) {
            if (res.count() == 7) break
            val titleAndLinkElem = elem.select("td.w_tit").select("a")
            val title = titleAndLinkElem.text()
            val typeElem = elem.select("td.w_type").select("i")
            val type = typeElem.text()
            val link = titleAndLinkElem.attr("href")
            val id = Utils.getWrId(titleAndLinkElem.attr("href"))!!.toInt()

            if (type == "Notice" || type == "Event")
                res.add(News(title, id, type, link))
        }

        return res
    }

    suspend fun getUserInfo(): User? {
        val t = getDocument(client, "https://www.piugame.com/my_page/play_data.php", true)
            ?: return null
        val profileBox = t.select("div.box0.inner.flex.vc.bgfix").first()
        val avatarBox = t.select("div.re.bgfix").first()

        val backgroundUri = getBackgroundImg(profileBox!!)
        val avatarUri = getBackgroundImg(avatarBox!!, false)

        val profileTitleAndName = t.select("div.name_w").select("p")

        val titleName = profileTitleAndName.first()!!.text()
        val username = profileTitleAndName.last()!!.text()
        val recentGame = t.select("div.time_w").select("li").last()!!.text()
            .replace("Recently Access Games : ", "")
        val coinValue = t.select("i.tt.en").first()!!.text()

        return User(username, titleName, backgroundUri, avatarUri, recentGame, coinValue, true)
    }

    private fun parseBestUserScoreElement(
        element: Element,
        bgs: MutableList<BgInfo>,
    ): BestUserScore? {
        val songName = element.select("div.song_name").select("p").first()?.text() ?: return null
        var bg = bgs.find { tt -> tt.song_name == songName }?.jacket
        if (bg == null) bg = "https://www.piugame.com/l_img/bg1.png"

        val diffElems = element.select("div.numw.flex.vc.hc")
        val typeDiffImgUri = getBackgroundImg(
            element.select("div.stepBall_in.flex.vc.col.hc.wrap.bgfix.cont").first()!!, false
        )
        val typeDiff = Utils.parseTypeDifficultyFromUriBestScore(typeDiffImgUri)!!

        val diff = diffElems.select("img").map { Utils.parseDifficultyFromUri(it.attr("src")) }
            .joinToString("").let { typeDiff.uppercase(Locale.ENGLISH) + it }

        val scoreInfoElement = element.select("ul.list.flex.vc.hc.wrap")
        val score = scoreInfoElement.select("span.num").text()
        val rankImg = scoreInfoElement.select("img").first()!!.attr("src")
        val rank = Utils.parseRankFromUri(rankImg).toString()
            .uppercase(Locale.ENGLISH).replace("_p", "+").replace("_P", "+")

        return BestUserScore(songName, bg, diff, score, rank)
    }

    private fun parseBestUserScores(
        t: Document,
        res: MutableList<BestUserScore>,
        bgs: MutableList<BgInfo>
    ): Boolean {
        if (t.select("div.no_con").isNotEmpty()) {
            res.add(
                BestUserScore(
                    "No scores",
                    "https://www.piugame.com/l_img/bg1.png",
                    "No scores",
                    "000,000",
                    "SSS+"
                )
            )
            return true
        }
        val scoreTable = t.select("ul.my_best_scoreList.flex.wrap")
        val scores = scoreTable.select("li").filter { element ->
            element.select("div.in").count() == 1
        }

        for (element in scores) {
            val bestUserScore = parseBestUserScoreElement(element, bgs)
            if (bestUserScore != null) res.add(bestUserScore)
        }

        return t.select("button.icon").isEmpty()
    }

    suspend fun getBestUserScores(
        page: Int? = null,
        lvl: String = "",
        res: MutableList<BestUserScore> = mutableListOf(),
        bgs: MutableList<BgInfo>,
    ): Pair<MutableList<BestUserScore>, Boolean>? {
        var uri = "$BASE_PIU_URL/my_page/my_best_score.php"
        var isRecent = false
        uri += "?lv=$lvl"
        if (page == null) {
            for (i in 1..2) {
                if (!isRecent) {
                    if (uri.contains("&page"))
                        uri = uri.dropLast(1) + i
                    else
                        uri += "&page=$i"
                    val t =
                        getDocument(client, uri, true) ?: return null
                    isRecent = parseBestUserScores(t, res, bgs = bgs)
                }
            }
        } else {
            uri += "&page=$page"
            val t =
                getDocument(client, uri, true) ?: return null
            isRecent = parseBestUserScores(t, res, bgs = bgs)
        }
        return Pair(res, isRecent)

    }

    suspend fun getLatestScores(length: Int): MutableList<LatestScore>? {
        val res: MutableList<LatestScore> = mutableListOf()

        val t = getDocument(client, "https://www.piugame.com/my_page/recently_played.php", true)
            ?: return null

        val scoreTable = t.select("ul.recently_playeList.flex.wrap")
        val scores = scoreTable.select("li").filter { element ->
            element.select("div.wrap_in").count() == 1
        }
        for (element in scores) {
            if (res.count() == length)
                break
            val songName = element.select("div.song_name.flex").select("p").text()

            val typeDiffImgUri = element.select("div.tw").select("img").attr("src")

            val typeDiff = Utils.parseTypeDifficultyFromUri(typeDiffImgUri)!!

            val bg = getBackgroundImg(element.select("div.in.bgfix").first()!!, false)

            val diffElems = element.select("div.imG")

            var diff = ""

            for (i in diffElems) {
                diff += Utils.parseDifficultyFromUri(i.select("img").attr("src"))
            }

            diff = typeDiff.uppercase(Locale.ENGLISH) + diff

            val scoreRankElement = element.select("div.li_in.ac")

            val score = scoreRankElement.select("i.tx").text()

            var rank = "F"

            if ("STAGE BREAK" !in score) {
                val rankImg = scoreRankElement.first()!!.select("img").attr("src")
                rank = Utils.parseRankFromUri(rankImg).toString()
                rank = rank.uppercase(Locale.ENGLISH).replace("_p", "+").replace("_P", "+")
                    .replace("X_", "Broken ")
            }

            val datePlay = element.select("p.recently_date_tt").text()
            res.add(
                LatestScore(
                    songName,
                    bg,
                    diff,
                    score,
                    rank,
                    Utils.convertDateFromSite(datePlay)
                )
            )
        }

        return res
    }
}

