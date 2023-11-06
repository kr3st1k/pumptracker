package dev.kr3st1k.piucompanion
import io.ktor.client.HttpClient
import io.ktor.client.features.cookies.ConstantCookiesStorage
import io.ktor.client.features.cookies.HttpCookies
import io.ktor.client.features.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.Cookie
import io.ktor.http.HttpHeaders

object RequestHandler{

    suspend fun checkIfLoginSuccess(cookie: String, ua: String): Boolean {
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
                    Cookie(cookieList[0],cookieList[1], domain = ".piugame.com"),
                    Cookie(cookieList[2],cookieList[3], domain = ".piugame.com"),
                    Cookie(cookieList[4],cookieList[5], domain = ".piugame.com"),
                    Cookie(cookieList[6],cookieList[7], domain = ".piugame.com"))

                defaultRequest {
                    headers {
                        append(HttpHeaders.UserAgent, ua)
                    }
                }


            }
        }



        val t = client.get<String>("https://piugame.com")

        return t.indexOf("bbs/logout.php") > 0;
    }
}