package com.kr3st1k.pumptracker.core.network

import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class TrustAllX509TrustManager : X509TrustManager {
    override fun getAcceptedIssuers(): Array<X509Certificate?> = arrayOfNulls(0)

    override fun checkClientTrusted(certs: Array<X509Certificate?>?, authType: String?) {}

    override fun checkServerTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
}

object SslSettings {

    fun getSslContext(): SSLContext {
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf<TrustManager>(TrustAllX509TrustManager()), null)
        return sslContext
    }

    fun getTrustManager(): X509TrustManager {
        return TrustAllX509TrustManager()
    }
}