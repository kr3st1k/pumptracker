package dev.kr3st1k.piucompanion.core.modules

import android.content.SharedPreferences
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
    }
}