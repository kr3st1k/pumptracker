package dev.kr3st1k.piucompanion.objects

import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url

class WebViewCookieStorage(private val cookies: List<Cookie>) : CookiesStorage {
    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        print("add_cookie_storage")
    }

    override fun close() {
        print("close_cookie_storage")
    }

    override suspend fun get(requestUrl: Url): List<Cookie> {
        return cookies.filter { it.domain!! == requestUrl.host }
    }
}