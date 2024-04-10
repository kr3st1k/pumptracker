package dev.kr3st1k.piucompanion.core.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.jsoup.nodes.Element
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Matcher
import java.util.regex.Pattern


object Utils
{
    private const val DIFFICULTY = "https://www\\.piugame\\.com/l_img/stepball/full/[a-zA-Z]_num_([0-9])\\.png"
    private const val DIFFICULTYTYPE = "https://www\\.piugame\\.com\\/l_img\\/stepball\\/full\\/([a-zA-Z])_text\\.png"
    private const val DIFFICULTYTYPEBEST = "https://www.piugame\\.com\\/l_img\\/stepball\\/full\\/([a-zA-Z])_bg\\.png"
    private const val RANK = "https://www\\.piugame\\.com\\/l_img\\/grade\\/(\\w+)\\.png"

    private var androidId: String = ""

    fun getAndroidId(): String {
        return androidId
    }

    @SuppressLint("HardwareIds")
    fun setDeviceId(context: Context) {
        try {
            val contentResolver = context.contentResolver
            androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun parseDifficultyFromUri(uri: String): String? = getFirstRegex(DIFFICULTY, uri)

    fun parseRankFromUri(uri: String): String? = getFirstRegex(RANK, uri)

    @Composable
    fun <T> rememberLiveData(
        liveData: LiveData<T>,
        lifecycleOwner: LifecycleOwner,
        initialValue: T,
    ): State<T> {
        val state = remember { mutableStateOf(initialValue) }
        DisposableEffect(lifecycleOwner, liveData) {
            val observer = Observer<T> { value ->
                state.value = value
            }
            liveData.observe(lifecycleOwner, observer)

            onDispose {
                liveData.removeObserver(observer)
            }
        }
        return state
    }


    fun parseTypeDifficultyFromUri(uri: String): String? = getFirstRegex(DIFFICULTYTYPE, uri)

    fun parseTypeDifficultyFromUriBestScore(uri: String): String? = getFirstRegex(DIFFICULTYTYPEBEST, uri)

    fun checkIfLoginSuccess(data: String): Boolean = data.indexOf("bbs/logout.php") > 0

    fun getBackgroundImg(element: Element, addDomain: Boolean = true): String {
        val style = element.attr("style")
        return (if (addDomain) "https://www.piugame.com" else "") + style.substringAfter("background-image:url('")
            .substringBefore("')")
    }

    fun getWrId(url: String): String? = getFirstRegex("wr_id=(\\d+)", url)

    private fun getFirstRegex(pattern: String, text: String): String?
    {
        val diffPattern = Pattern.compile(pattern)
        val matcher: Matcher = diffPattern.matcher(text)
        if (matcher.find()) {
            return matcher.group(1)
        }
        return ""
    }

    fun convertDateFromSite(date: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val inputZoneOffset = date.substring(date.lastIndexOf("(") + 1, date.lastIndexOf(")"))
        val inputZoneId = ZoneId.of(inputZoneOffset)
        val inputDateTime = LocalDateTime.parse(date.substring(0, 19), inputFormatter)
        val inputZonedDateTime = ZonedDateTime.of(inputDateTime, inputZoneId)

        val outputZoneId = ZoneId.systemDefault()
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val outputDateTime = inputZonedDateTime.withZoneSameInstant(outputZoneId)

        return outputDateTime.format(outputFormatter)
    }

}