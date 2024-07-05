package dev.kr3st1k.piucompanion.di

import android.content.SharedPreferences
import dev.kr3st1k.piucompanion.core.network.data.User
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginManager : KoinComponent {
    private val sharedPreferences: SharedPreferences by inject()

    fun hasLoginData(): Boolean {
        return sharedPreferences.contains("login") && sharedPreferences.contains("password")
    }

    fun getLoginData(): Pair<String?, String?> {
        val login = sharedPreferences.getString("login", null)
        val password = sharedPreferences.getString("password", null)
        return Pair(login, password)
    }

    fun saveLoginData(login: String, password: String) {
        sharedPreferences.edit().putString("login", login).apply()
        sharedPreferences.edit().putString("password", password).apply()
    }

    fun removeLoginData() {
        sharedPreferences.edit().remove("login").apply()
        sharedPreferences.edit().remove("password").apply()
        sharedPreferences.edit().remove("username").apply()
        sharedPreferences.edit().remove("titleName").apply()
        sharedPreferences.edit().remove("backgroundUri").apply()
        sharedPreferences.edit().remove("avatarUri").apply()
        sharedPreferences.edit().remove("recentGameAccess").apply()
        sharedPreferences.edit().remove("coinValue").apply()
        sharedPreferences.edit().remove("pumbility").apply()
    }

    fun getUserData(): User {
        if (sharedPreferences.getString("username", "") == "")
            return User()
        return User(
            sharedPreferences.getString("username", "")!!,
            sharedPreferences.getString("titleName", "")!!,
            sharedPreferences.getString("backgroundUri", "")!!,
            sharedPreferences.getString("avatarUri", "")!!,
            sharedPreferences.getString("recentGameAccess", "")!!,
            sharedPreferences.getString("coinValue", "")!!,
            sharedPreferences.getString("pumbility", null),
            true,
        )
    }

    fun saveUserData(user: User) {
        sharedPreferences.edit().putString("username", user.username).apply()
        sharedPreferences.edit().putString("avatarUri", user.avatarUri).apply()
        sharedPreferences.edit().putString("backgroundUri", user.backgroundUri).apply()
        sharedPreferences.edit().putString("recentGameAccess", user.recentGameAccess).apply()
        sharedPreferences.edit().putString("titleName", user.titleName).apply()
        sharedPreferences.edit().putString("coinValue", user.coinValue).apply()
        if (user.pumbility != null)
            sharedPreferences.edit().putString("pumbility", user.pumbility).apply()
    }
}