package dev.kr3st1k.piucompanion.core.network

import dev.kr3st1k.piucompanion.MainActivity
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies

object KtorInstance {
    private val client = HttpClient(OkHttp) {
        engine {
            //addInterceptor(logInterceptor()) TODO only in debug
        }

        install(ContentEncoding) {
            gzip()
        }

        install(UserAgent) {
            agent = MainActivity.userAgent
        }

        install(HttpCookies) {
            storage = AcceptAllCookiesStorage()
        }

        followRedirects = true
    }

    public fun getHttpClient(): HttpClient = client
}