package com.kr3st1k.pumptracker.core.network

//import dev.kr3st1k.piucompanion.MainActivity
//import io.ktor.client.engine.okhttp.OkHttp
//import io.ktor.client.plugins.UserAgent
import com.kr3st1k.pumptracker.getPlatformHttpClient
import io.ktor.client.*

//import okhttp3.logging.HttpLoggingInterceptor

object KtorInstance {

    private val client = getPlatformHttpClient()

    public fun getHttpClient(): HttpClient = client
}