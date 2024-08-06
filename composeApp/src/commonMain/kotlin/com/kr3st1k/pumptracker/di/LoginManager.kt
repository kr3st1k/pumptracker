package com.kr3st1k.pumptracker.di

//import android.content.SharedPreferences
import com.kr3st1k.pumptracker.core.network.NetworkRepositoryImpl
import com.russhwolf.settings.Settings
import com.kr3st1k.pumptracker.core.network.data.User
import com.kr3st1k.pumptracker.isDarkTheme
import com.kr3st1k.pumptracker.isDynamicColors
import com.kr3st1k.pumptracker.isSystemDefault
import com.kr3st1k.pumptracker.updateFrame
import com.russhwolf.settings.ObservableSettings
//import dev.kr3st1k.piucompanion.core.network.data.User
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginManager : KoinComponent {
    val settings: Settings = Settings()

    fun hasLoginData(): Boolean {
        return settings.hasKey("login") && settings.hasKey("password")
    }

    fun getLoginData(): Pair<String?, String?> {
        val login = settings.getStringOrNull("login")
        val password = settings.getStringOrNull("password")
        return Pair(login, password)
    }

    fun saveLoginData(login: String, password: String) {
        settings.putString("login", login)
        settings.putString("password", password)
    }

    fun getIsDynamicColor(): Boolean {
        return settings.getBoolean("dynamic_color", true)
    }

    fun getIsSystemDefault(): Boolean {
        return settings.getBoolean("system_default", true)
    }

    fun getIsDarkTheme(): Boolean {
        return settings.getBoolean("dark_theme", true)
    }

    fun saveIsDynamicColor(value: Boolean) {
        settings.putBoolean("dynamic_color", value)
        isDynamicColors.value = value
        updateFrame()
    }

    fun saveIsSystemDefault(value: Boolean) {
        settings.putBoolean("system_default", value)
        isSystemDefault.value = value
        updateFrame()
    }

    fun saveIsDarkTheme(value: Boolean) {
        settings.putBoolean("system_default", false)
        isSystemDefault.value = false
        settings.putBoolean("dark_theme", value)
        isDarkTheme.value = value
        updateFrame()
    }

    fun removeLoginData() {
        settings.remove("login")
        settings.remove("password")
        settings.remove("username")
        settings.remove("titleName")
        settings.remove("backgroundUri")
        settings.remove("avatarUri")
        settings.remove("recentGameAccess")
        settings.remove("coinValue")
        settings.remove("pumbility")
    }

    fun getUserData(): User {
        if (!settings.hasKey("username"))
            return User()
        else
        return User(
            settings.getString("username", ""),
            settings.getString("titleName", ""),
            settings.getString("backgroundUri", ""),
            settings.getString("avatarUri", ""),
            settings.getString("recentGameAccess", ""),
            settings.getString("coinValue", ""),
            settings.getStringOrNull("pumbility"),
            true,
        )
    }

    fun saveUserData(user: User) {
        settings.putString("username", user.username)
        settings.putString("avatarUri", user.avatarUri)
        settings.putString("backgroundUri", user.backgroundUri)
        settings.putString("recentGameAccess", user.recentGameAccess)
        settings.putString("titleName", user.titleName)
        settings.putString("coinValue", user.coinValue)
        if (user.pumbility != null)
            settings.putString("pumbility", user.pumbility!!)
    }
}