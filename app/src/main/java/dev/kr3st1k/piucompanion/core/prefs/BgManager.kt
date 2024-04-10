package dev.kr3st1k.piucompanion.core.prefs

import dev.kr3st1k.piucompanion.core.network.NetworkRepositoryImpl
import dev.kr3st1k.piucompanion.core.network.RequestHandler
import dev.kr3st1k.piucompanion.core.network.data.BgInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class BgManager : KoinComponent {
    private val filesDir: File by inject()
    private val jsonWorker = Json { prettyPrint = true; ignoreUnknownKeys = true }

    fun checkAndSaveNewUpdatedFiles() {
        CoroutineScope(Dispatchers.IO).launch {
            val newUpdateValue = NetworkRepositoryImpl.getUpdateInfo()
            val currentValue = readUpdateValue()
            if (currentValue != newUpdateValue) {
                saveNewUpdateValue(newUpdateValue)
                val bgJson = RequestHandler.getBgJson()
                saveBgJson(bgJson)
            }
        }
    }

    fun readBgJson(): MutableList<BgInfo> {
        val file = File(filesDir, "piu_bg_database.json")
        if (!file.exists()) {
            file.createNewFile()
            saveBgJson(mutableListOf())
        }
        val inputStream = file.inputStream()
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        return jsonWorker.decodeFromString(jsonString)
    }

    private fun saveBgJson(list: MutableList<BgInfo>) {
        val listText = jsonWorker.encodeToString(list)
        val outputStream = File(filesDir, "piu_bg_database.json").outputStream()
        outputStream.write(listText.toByteArray())
        outputStream.close()
    }

    fun readUpdateValue(): String? {
        val file = File(filesDir, "update.txt")
        if (!file.exists()) {
            file.createNewFile()
            saveNewUpdateValue("")
        }
        val inputStream = file.inputStream()
        val reader = BufferedReader(InputStreamReader(inputStream))
        val currentValue = reader.readLine()
        reader.close()
        inputStream.close()
        return currentValue
    }

    private fun saveNewUpdateValue(newValue: String) {
        val outputStream = File(filesDir, "update.txt").outputStream()
        outputStream.write(newValue.toByteArray())
        outputStream.close()
    }
}