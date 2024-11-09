package com.kr3st1k.pumptracker.di

import com.kr3st1k.pumptracker.core.network.KtorInstance
import com.kr3st1k.pumptracker.core.network.NetworkRepositoryImpl
import com.kr3st1k.pumptracker.core.network.NetworkRepositoryImpl.client
import com.kr3st1k.pumptracker.core.network.data.BgInfo
import com.kr3st1k.pumptracker.getPlatform
import com.kr3st1k.pumptracker.getPlatformForJKS
import com.kr3st1k.pumptracker.getUserDirectory
import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.FileSystem
import org.koin.core.component.KoinComponent
import java.io.ByteArrayInputStream

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

    //NOT BG PART BUT IDC
    fun readJKS(): ByteArrayInputStream {
        if (!FileSystem.SYSTEM.exists(getUserDirectory().resolve("cert.jks"))) {
            FileSystem.SYSTEM.write(getUserDirectory().resolve("cert.jks"), mustCreate = true) {
                write(jsonWorker.encodeToString(mutableListOf(BgInfo("", ""))).toByteArray())
            }
            saveJKS()
        }
        val file = FileSystem.SYSTEM.read(getUserDirectory().resolve("cert.jks")) {
            readByteArray()
        }

        return ByteArrayInputStream(file)
    }

    fun saveJKS() {
        if (!FileSystem.SYSTEM.exists(getUserDirectory().resolve("cert.jks"))) {
            FileSystem.SYSTEM.write(getUserDirectory().resolve("cert.jks"), mustCreate = true) {
                write(jsonWorker.encodeToString(mutableListOf(BgInfo("", ""))).toByteArray())
            }
            if (InternetManager().hasInternetStatus())
                CoroutineScope(Dispatchers.IO).launch {
                    NetworkRepositoryImpl.downloadJKS(
                        getPlatformForJKS(getPlatform()),
                        getUserDirectory().resolve("cert.jks")
                    )
                }
        } else {
            client = KtorInstance.getHttpClient()
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