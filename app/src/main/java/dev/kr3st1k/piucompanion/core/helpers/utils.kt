package dev.kr3st1k.piucompanion.core.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import com.fleeksoft.ksoup.nodes.Element
import dev.kr3st1k.piucompanion.core.db.data.score.BestScore
import dev.kr3st1k.piucompanion.core.db.repository.ScoresRepository
import dev.kr3st1k.piucompanion.core.network.NetworkRepositoryImpl
import java.net.URL
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.ceil


object Utils
{
    private const val DIFFICULTY = "https://www\\.piugame\\.com/l_img/stepball/full/[a-zA-Z]_num_([0-9])\\.png"
    private const val DIFFICULTYTYPE = "https://www\\.piugame\\.com\\/l_img\\/stepball\\/full\\/([a-zA-Z])_text\\.png"
    private const val DIFFICULTYTYPEBEST = "https://www.piugame\\.com\\/l_img\\/stepball\\/full\\/([a-zA-Z])_bg\\.png"
    private const val RANK = "https://www\\.piugame\\.com\\/l_img\\/grade\\/(\\w+)\\.png"

    private var androidId: String = ""

    fun pointMultiplier(value: String): Float {
        val score = value.replace(",", "").toInt()
        if (score >= 995000) return 1.5F    // SSS+
        if (score >= 990000) return 1.44F   // SSS
        if (score >= 985000) return 1.38F   // SS+
        if (score >= 980000) return 1.32F   // SS
        if (score >= 975000) return 1.26F   // S+
        if (score >= 970000) return 1.2F    // S
        if (score >= 960000) return 1.15F   // AAA+
        if (score >= 950000) return 1.1F    // AAA
        if (score >= 925000) return 1.05F   // AA+
        if (score >= 900000) return 1F      // AA
        if (score >= 825000) return 0.9F    // A+
        if (score >= 750000) return 0.8F    // A
        if (score >= 650000) return 0.7F    // B
        if (score >= 550000) return 0.6F    // C
        if (score >= 450000) return 0.5F    // D
        return 0.4F                         // F
    }

    private fun levelMultiplier(value: Int): Float {
        if (value < 10) return 0F;
        if (value == 10) return 100F;
        return (value - 10) * 10 + levelMultiplier(value - 1);
    }

    fun getPoints(lvl: String, score: String): Int {
        val points = levelMultiplier(lvl.filter { it2 -> it2.isDigit() }
            .map { it2 -> it2.toString().toInt() }.joinToString("")
            .toInt()
        ) * pointMultiplier(score)
        return ceil(points).toInt()
    }


    fun getAndroidId(): String {
        return androidId
    }

    fun generateHashForScore(
        score: String,
        difficulty: String,
        songName: String,
        date: String = "0"
    ): String {
        val input = "$score-$difficulty-$songName-$date"
        val md = MessageDigest.getInstance("SHA-256")
        val byteArray = md.digest(input.toByteArray())
        return byteArray.joinToString("") { String.format("%02x", it) }
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


    fun parseTypeDifficultyFromUri(uri: String): String? = getFirstRegex(DIFFICULTYTYPE, uri)

    fun parseTypeDifficultyFromUriBestScore(uri: String): String? = getFirstRegex(DIFFICULTYTYPEBEST, uri)

    fun checkIfLoginSuccess(data: String): Boolean = data.indexOf("bbs/logout.php") > 0

    fun removeUrlParameters(urlString: String): String {
        return try {
            val url = URL(urlString)
            url.protocol + "://" + url.host + url.path
        } catch (e: Exception) {
            urlString // Return the original string if there's an error parsing the URL
        }
    }

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

    fun convertDateToLocalDateTime(date: String): ZonedDateTime? {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val inputZoneOffset = date.substring(date.lastIndexOf("(") + 1, date.lastIndexOf(")"))
        val inputZoneId = ZoneId.of(inputZoneOffset)
        val inputDateTime = LocalDateTime.parse(date.substring(0, 19), inputFormatter)
        val inputZonedDateTime = ZonedDateTime.of(inputDateTime, inputZoneId)

        val outputZoneId = ZoneId.systemDefault()
        val outputDateTime = inputZonedDateTime.withZoneSameInstant(outputZoneId)
        return outputDateTime
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

    suspend fun getNewBestScoresFromWeb(
        nowPage: MutableIntState,
        pageCount: MutableIntState,
        needAuth: MutableState<Boolean>,
        scoresRepository: ScoresRepository
    ): List<BestScore> {
        var isInside = false
        var tmp = NetworkRepositoryImpl.getBestUserScores(page = nowPage.intValue)
        if (tmp == null) {
            needAuth.value = true
            return mutableListOf()
        }
        pageCount.intValue = tmp.lastPageNumber
        val scoresTmp = mutableListOf<BestScore>()
        val scoresFromDb = scoresRepository.getBestScores()
        while (nowPage.intValue <= pageCount.intValue && !isInside) {
            for (it in tmp!!.res) {
                val score = BestScore(
                    songName = it.songName,
                    difficulty = it.difficulty,
                    score = it.score,
                    rank = it.rank,
                    hash = generateHashForScore(
                        "0",
                        it.difficulty,
                        it.songName
                    ),
                    pumbilityScore = getPoints(it.difficulty, it.score)
                )

                if (scoresFromDb.contains(score)) {
                    isInside = true
                    break
                }

                scoresTmp.add(score)
            }
            if (nowPage.intValue < pageCount.intValue) {
                nowPage.intValue += 1
                tmp = NetworkRepositoryImpl.getBestUserScores(page = nowPage.intValue)
            } else {
                break
            }
        }
        return scoresTmp
    }

}