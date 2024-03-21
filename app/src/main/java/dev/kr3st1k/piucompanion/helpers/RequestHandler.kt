package dev.kr3st1k.piucompanion.helpers
import androidx.compose.ui.text.toUpperCase
import dev.kr3st1k.piucompanion.objects.LatestScore
import dev.kr3st1k.piucompanion.objects.NewsBanner
import dev.kr3st1k.piucompanion.objects.NewsThumbnailObject
import dev.kr3st1k.piucompanion.objects.User
import dev.kr3st1k.piucompanion.screens.components.Utils
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.cookies.ConstantCookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.request.get
import io.ktor.http.Cookie
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern

object RequestHandler{
    private fun getClientWithCookies(cookie: String, ua: String): HttpClient {
        val pairs = cookie.split(";").filter { it.isNotEmpty() }
        val cookieList = ArrayList<String>();
        for (pair in pairs) {
            val parts = pair.split("=")
            if (parts.size == 2) {
                cookieList.add(parts[0].trim())
                cookieList.add(parts[1].trim())
            }
        }

        val client = HttpClient {
            install(HttpCookies) {
                storage = ConstantCookiesStorage(
                    Cookie(cookieList[0], cookieList[1], domain = ".piugame.com"),
                    Cookie(cookieList[2], cookieList[3], domain = ".piugame.com"),
                    Cookie(cookieList[4], cookieList[5], domain = ".piugame.com"),
                    Cookie(cookieList[6], cookieList[7], domain = ".piugame.com")
                )


                headers {
                    append(HttpHeaders.UserAgent, ua)
                }



            }
        }
        return client
    }

    suspend fun checkIfLoginSuccess(cookie: String, ua: String): Boolean {

        println(cookie.split(";").size)

        if (cookie == "G53public_htmlPHPSESSID=1; PHPSESSID=1; sid=1; dn=1; dk=1; ld=1; df=f; cf=c")
            return false

        val client = getClientWithCookies(cookie, ua)

        println(ua);

        val t = client.get("https://piugame.com")
        val stringBody: String = t.body()
        return stringBody.indexOf("bbs/logout.php") > 0;
    }

    private fun getBackgroundImg(element: Element, addDomain: Boolean = true): String {
        val style = element.attr("style")
        return (if (addDomain) "https://www.piugame.com" else "") + style.substringAfter("background-image:url('").substringBefore("')");
    }

    suspend fun getDocument(client: HttpClient, uri: String): Document {
        val req = client.get(uri)
        val reqBody: String = req.body()
        return Jsoup.parse(reqBody);
    }

    suspend fun getNewsBanners(cookie: String, ua: String): MutableList<NewsBanner>
    {
        val client = getClientWithCookies(cookie, ua)

        val t = this.getDocument(client, "https://www.piugame.com")

        val r = t.select("a.img.resize.bgfix");
        val uniqueElements: MutableList<Element> = mutableListOf()
        val res: MutableList<NewsBanner> = mutableListOf();

        for (element in r) {
            if (!uniqueElements.any { it.attr("href") == element.attr("href") }) {
                uniqueElements.add(element)
            }
        }

        for (element in uniqueElements) {
            res.add(NewsBanner(Utils.getWrId(element.attr("href"))?.toInt() ?: 0, getBackgroundImg(element), element.attr("href")))
        }
        return res;
    }

    suspend fun getNewsList(cookie: String, ua: String): MutableList<NewsThumbnailObject>
    {
        val client = getClientWithCookies(cookie, ua)
        val res: MutableList<NewsThumbnailObject> = mutableListOf();

        val t = this.getDocument(client, "https://www.piugame.com/phoenix_notice")
        val table = t.select("tbody")

        val trElements: Elements = table.select("tr")

        for (elem in trElements)
        {
            if (res.count() == 7)
                break
            val titleAndLinkElem = elem.select("td.w_tit").select("a");
            val title = titleAndLinkElem.text()
            val typeElem = elem.select("td.w_type").select("i")
            val type = typeElem.text()
            val link = titleAndLinkElem.attr("href")
            val id = Utils.getWrId(titleAndLinkElem.attr("href"))!!.toInt()

            if (type == "Notice" || type == "Event")
                res.add(NewsThumbnailObject(title, id, type, link))
        }

        return res;
    }

    suspend fun getUserInfo(cookie: String, ua: String): User
    {
        val client = getClientWithCookies(cookie, ua)
        val t = this.getDocument(client, "https://www.piugame.com/my_page/play_data.php")

        val profileBox = t.select("div.box0.inner.flex.vc.bgfix")
        val avatarBox = t.select("div.re.bgfix")

        val backgroundUri = getBackgroundImg(profileBox.first()!!)
        val avatarUri = getBackgroundImg(avatarBox.first()!!, false)
        val titleName = t.select("p.t1.en.col4").first()!!.text()
        val username = t.select("p.t2.en").first()!!.text()
        val recentGame = t.select("div.time_w").select("li").last()!!.text()
        val coinValue = t.select("i.tt.en").first()!!.text()

        return User(username, titleName, backgroundUri, avatarUri, recentGame, coinValue, true)
    }



    suspend fun getLatestScores(cookie: String, ua: String, length: Int): MutableList<LatestScore>
    {
        val client = getClientWithCookies(cookie, ua)
        val res: MutableList<LatestScore> = mutableListOf();

        val t = this.getDocument(client,"https://www.piugame.com/my_page/recently_played.php")

        val scoreTable = t.select("ul.recently_playeList.flex.wrap")
        val scores = scoreTable.select("li").filter { element ->
            element.select("div.wrap_in").count() == 1
        }
        for (element in scores)
        {
            if (res.count() == length)
                break
            val songName = element.select("div.song_name.flex").select("p").text()

            val typeDiffImgUri = element.select("div.tw").select("img").attr("src")

            val typeDiff = Utils.parseTypeDifficultyFromUri(typeDiffImgUri)!!

            val bg = getBackgroundImg(element.select("div.in.bgfix").first()!!, false)

            val diffElems = element.select("div.imG")

            var diff = ""

            for (i in diffElems)
            {
                diff += Utils.parseDifficultyFromUri(i.select("img").attr("src"))
            }

            diff = typeDiff.uppercase(Locale.ENGLISH) + diff

            val scoreRankElement = element.select("div.li_in.ac")

            val score = scoreRankElement.select("i.tx").text()

            var rank = "F"

            if ("STAGE BREAK" !in score)
            {
                val rankImg = scoreRankElement.first()!!.select("img").attr("src")
                rank = Utils.parseRankFromUri(rankImg).toString()
                rank = rank.uppercase(Locale.ENGLISH).replace("_p","+").replace("_P","+").replace("X_", "Broken ")
            }

            val datePlay = element.select("p.recently_date_tt").text()
            res.add(LatestScore(songName,bg,diff,score,rank,Utils.convertDateFromSite(datePlay)))
        }

        return res
    }
}