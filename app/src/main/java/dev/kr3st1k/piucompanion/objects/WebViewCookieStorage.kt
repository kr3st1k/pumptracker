package dev.kr3st1k.piucompanion.objects

import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url

class WebViewCookieStorage(private val cookies: MutableList<Cookie>) : CookiesStorage {
    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        val existingCookieIndex =
            cookies.indexOfFirst { it.name == cookie.name && it.domain == cookie.domain }
        if (existingCookieIndex != -1) {
            val updatedCookie = cookie.copy(value = cookie.value, expires = cookie.expires)
            cookies[existingCookieIndex] = updatedCookie
        } else {
            cookies.add(cookie)
        }
    }

    override fun close() {
        println("close_cookie_storage")
    }

    override suspend fun get(requestUrl: Url): MutableList<Cookie> {
        return cookies.filter { it.domain?.let { it1 -> requestUrl.host.contains(it1) } == true }
            .toMutableList()
    }
}
