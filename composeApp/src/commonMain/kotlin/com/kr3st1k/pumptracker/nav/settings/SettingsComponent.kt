package com.kr3st1k.pumptracker.nav.settings

import com.arkivanov.decompose.ComponentContext

class SettingsComponent(
    val navigateToLogin: () -> Unit,
    componentContext: ComponentContext
) : ComponentContext by componentContext {

    fun navigateToStart() {
        navigateToLogin()
    }

}