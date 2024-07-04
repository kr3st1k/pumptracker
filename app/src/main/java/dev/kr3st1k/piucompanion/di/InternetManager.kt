package dev.kr3st1k.piucompanion.di

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InternetManager : KoinComponent {
    private val connectivityManager: ConnectivityManager by inject()

    fun hasInternetStatus(): Boolean {
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

    }
}