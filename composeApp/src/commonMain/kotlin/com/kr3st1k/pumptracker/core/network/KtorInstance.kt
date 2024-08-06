package com.kr3st1k.pumptracker.core.network

//import dev.kr3st1k.piucompanion.MainActivity
import com.kr3st1k.pumptracker.getPlatformHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.plugins.*
//import io.ktor.client.engine.okhttp.OkHttp
//import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
//import okhttp3.logging.HttpLoggingInterceptor

object KtorInstance {

    private val client = HttpClient(getPlatformHttpClient()) {
        engine {
//            addInterceptor(logInterceptor())
        }

        install(HttpCache)

        install(ContentEncoding) {
            gzip()
        }

        install(UserAgent) {
            agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36"
        }

        install(HttpCookies) {
            storage = AcceptAllCookiesStorage()
        }

        followRedirects = true
    }

    public fun getHttpClient(): HttpClient = client
}