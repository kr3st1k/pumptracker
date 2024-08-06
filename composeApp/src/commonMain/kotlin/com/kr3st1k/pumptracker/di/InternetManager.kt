package com.kr3st1k.pumptracker.di
import dev.tmapps.konnection.Konnection
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InternetManager : KoinComponent {
    private val konnection = Konnection.instance

    fun hasInternetStatus(): Boolean {
        return konnection.isConnected()
    }
}