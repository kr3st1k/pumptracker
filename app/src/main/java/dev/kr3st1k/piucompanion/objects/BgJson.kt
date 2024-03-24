package dev.kr3st1k.piucompanion.objects

import android.content.Context
import dev.kr3st1k.piucompanion.helpers.RequestHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

@Serializable
data class BgInfo (
    val jacket: String,
    val song_name: String
)

fun checkAndSaveNewUpdatedFiles(context: Context)
{
    CoroutineScope(Dispatchers.IO).launch {
        val newUpdateValue = RequestHandler.getUpdateInfo()
        val currentValue = readUpdateValue(context)
        if (currentValue != newUpdateValue) {
            saveNewUpdateValue(context, newUpdateValue)
            val bgJson = RequestHandler.getBgJson()
            saveBgJson(context, bgJson)
        }
    }
}

fun readBgJson(context: Context): MutableList<BgInfo>
{
    val file = File(context.filesDir,"piu_bg_database.json")
    if (!file.exists()) {
        saveBgJson(context, mutableListOf())
    }
    val inputStream = context.openFileInput("piu_bg_database.json")
    val jsonString = inputStream.bufferedReader().use { it.readText() }
    val json = Json { ignoreUnknownKeys = true }
    return json.decodeFromString(jsonString)

}

fun saveBgJson(context: Context, list: MutableList<BgInfo>)
{
    val json = Json { prettyPrint = true; ignoreUnknownKeys = true }
    val listText = json.encodeToString(list)
    val outputStream = context.openFileOutput("piu_bg_database.json", Context.MODE_PRIVATE)
    outputStream.write(listText.toByteArray())
    outputStream.close()
}

fun readUpdateValue(context: Context): String? {
    val file = File(context.filesDir,"update.txt")
    if (!file.exists()) {
        saveNewUpdateValue(context, "")
    }
    val inputStream = context.openFileInput("update.txt")
    val reader = BufferedReader(InputStreamReader(inputStream))
    val currentValue = reader.readLine()
    reader.close()
    inputStream.close()
    return currentValue
}

fun saveNewUpdateValue(context: Context, newValue: String) {
    val outputStream = context.openFileOutput("update.txt", Context.MODE_PRIVATE)
    outputStream.write(newValue.toByteArray())
    outputStream.close()
}