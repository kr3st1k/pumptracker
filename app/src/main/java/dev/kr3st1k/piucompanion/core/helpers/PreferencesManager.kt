package dev.kr3st1k.piucompanion.core.helpers

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(var context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("Stuff_pref", Context.MODE_PRIVATE)

    fun saveData(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun removeLoginData() {
        saveData("cookies", "")
    }
}