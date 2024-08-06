package com.kr3st1k.pumptracker.di

import com.kr3st1k.pumptracker.core.network.NetworkRepositoryImpl
import com.kr3st1k.pumptracker.core.network.data.BgInfo
import com.kr3st1k.pumptracker.getUserDirectory
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.SYSTEM
import org.koin.core.component.KoinComponent

class BgManager : KoinComponent {
    private val jsonWorker = Json { prettyPrint = true; ignoreUnknownKeys = true }

    fun checkAndSaveNewUpdatedFiles() {
        if (InternetManager().hasInternetStatus())
            CoroutineScope(Dispatchers.IO).launch {
                val newUpdateValue = NetworkRepositoryImpl.getUpdateInfo().replace("\n", "") // WTF
                val currentValue = readUpdateValue()
                if (currentValue != newUpdateValue) {
                    saveNewUpdateValue(newUpdateValue)
                    val bgJson = NetworkRepositoryImpl.getBgJson()
                    saveBgJson(bgJson)
                }
            }
    }

    fun readBgJson(): MutableList<BgInfo> {
        if (!FileSystem.SYSTEM.exists(getUserDirectory().resolve("piu_bg_database.json")))
            FileSystem.SYSTEM.write(getUserDirectory().resolve("piu_bg_database.json"), mustCreate = true) {
                write(jsonWorker.encodeToString(mutableListOf(BgInfo("",""))).toByteArray())
            }
        val file = FileSystem.SYSTEM.read(getUserDirectory().resolve("piu_bg_database.json")) {
            readUtf8()
        }

        return jsonWorker.decodeFromString(file)
    }

    private fun saveBgJson(list: MutableList<BgInfo>) {
        val listText = jsonWorker.encodeToString(list)
        val file = FileSystem.SYSTEM.write(getUserDirectory().resolve("piu_bg_database.json")) {
            write(listText.toByteArray())
        }
        file.close()
    }

    fun readUpdateValue(): String {
        if (!FileSystem.SYSTEM.exists(getUserDirectory().resolve("update.txt")))
            FileSystem.SYSTEM.write(getUserDirectory().resolve("update.txt"), mustCreate = true) {
                write("".toByteArray())
            }
        val file = FileSystem.SYSTEM.read(getUserDirectory().resolve("update.txt")) {
            readUtf8()
        }
        return file
    }

    private fun saveNewUpdateValue(newValue: String) {
        val file = FileSystem.SYSTEM.write(getUserDirectory().resolve("update.txt")) {
            write(newValue.toByteArray())
        }
        file.close()
    }
}