package dev.kr3st1k.piucompanion.core.network

import okhttp3.logging.HttpLoggingInterceptor

fun logInterceptor(): HttpLoggingInterceptor {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
    return loggingInterceptor
}