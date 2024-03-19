package dev.kr3st1k.piucompanion.helpers
import dev.kr3st1k.piucompanion.objects.NewsBanner
import dev.kr3st1k.piucompanion.objects.NewsThumbnailObject
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

        val client = getClientWithCookies(cookie, ua)

        println(ua);

        val t = client.get("https://piugame.com")
        val stringBody: String = t.body()
        return stringBody.indexOf("bbs/logout.php") > 0;
    }

    private fun getWrId(url: String): String?
    {
        val pattern = Pattern.compile("wr_id=(\\d+)")
        val matcher: Matcher = pattern.matcher(url)

        if (matcher.find()) {
            return matcher.group(1);
        }
        return ""
    }

    private fun getBackgroundImgNewsBanner(element: Element): String {
        val style = element.attr("style")
        return "https://www.piugame.com" + style.substringAfter("background-image:url('").substringBefore("')");
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
            res.add(NewsBanner(getWrId(element.attr("href"))?.toInt() ?: 0, getBackgroundImgNewsBanner(element), element.attr("href")))
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
            if (res.count() == 5)
                break
            val titleAndLinkElem = elem.select("td.w_tit").select("a");
            val title = titleAndLinkElem.text()
            val typeElem = elem.select("td.w_type").select("i")
            val type = typeElem.text()
            val link = titleAndLinkElem.attr("href")
            val id = getWrId(titleAndLinkElem.attr("href"))!!.toInt()

            if (type == "Notice" || type == "Event")
                res.add(NewsThumbnailObject(title, id, type, link))
        }

        return res;
    }
}