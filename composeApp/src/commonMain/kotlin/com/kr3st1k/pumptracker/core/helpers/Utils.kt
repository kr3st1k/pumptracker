package com.kr3st1k.pumptracker.core.helpers

import kotlinx.datetime.*
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import com.fleeksoft.ksoup.nodes.Element
import com.kr3st1k.pumptracker.core.db.data.score.BestScore
import com.kr3st1k.pumptracker.core.db.repository.ScoresRepository
import com.kr3st1k.pumptracker.core.network.NetworkRepositoryImpl
import korlibs.crypto.sha256
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.math.ceil


object Utils
{
    private const val DIFFICULTY = "https://www\\.piugame\\.com/l_img/stepball/full/[a-zA-Z]_num_([0-9])\\.png"
    private const val DIFFICULTYTYPE = "https://www\\.piugame\\.com\\/l_img\\/stepball\\/full\\/([a-zA-Z])_text\\.png"
    private const val DIFFICULTYTYPEBEST = "https://www.piugame\\.com\\/l_img\\/stepball\\/full\\/([a-zA-Z])_bg\\.png"
    private const val RANK = "https://www\\.piugame\\.com\\/l_img\\/grade\\/(\\w+)\\.png"

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


    fun generateHashForScore(
        score: String,
        difficulty: String,
        songName: String,
        date: String = "0"
    ): String {
        val input = "$score-$difficulty-$songName-$date"
        val byteArray = input.encodeToByteArray()
        val sha256Digest = byteArray.sha256()
        return sha256Digest.hex
    }

    fun parseDifficultyFromUri(uri: String): String? = getFirstRegex(DIFFICULTY, uri)

    fun parseRankFromUri(uri: String): String? = getFirstRegex(RANK, uri)


    fun parseTypeDifficultyFromUri(uri: String): String? = getFirstRegex(DIFFICULTYTYPE, uri)

    fun parseTypeDifficultyFromUriBestScore(uri: String): String? = getFirstRegex(DIFFICULTYTYPEBEST, uri)

    fun checkIfLoginSuccess(data: String): Boolean = data.indexOf("bbs/logout.php") > 0

    fun removeUrlParameters(urlString: String): String {
        return try {
            val urlParts = urlString.split("?")
            val baseUrl = urlParts[0]
            val urlJson = Json.parseToJsonElement("""{"url": "$baseUrl"}""")
            val urlObject = urlJson.jsonObject
            val url = urlObject["url"]?.jsonPrimitive?.content
            url ?: urlString // Return the original string if there's an error parsing the URL
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
        val diffPattern = Regex(pattern)
        val matcher: MatchResult? = diffPattern.find(text)
        if (matcher != null) {
            return matcher.groups[1]!!.value
        }
        return null
    }

    fun convertDateToLocalDateTime(date: String): LocalDateTime {
        val inputPattern = "yyyy-MM-dd HH:mm:ss"
        
        val inputDateTime = LocalDateTime.parse(date.substring(0, 19).replace(" ", "T"))
        val inputZoneOffset = date.substring(date.lastIndexOf("(GMT") + 4, date.lastIndexOf(")"))
    
        val inputTimeZone = TimeZone.of("Etc/GMT${inputZoneOffset.replace(":", "")}")
    
        val instant = inputDateTime.toInstant(inputTimeZone)
    
        val outputTimeZone = TimeZone.currentSystemDefault()
        val outputDateTime = instant.toLocalDateTime(outputTimeZone)
        return outputDateTime
    }

    fun convertDateFromSite(date: String): String {
        val inputPattern = "yyyy-MM-dd HH:mm:ss"
    
        // Parse the input date-time string
        val dateTimeString = date.substring(0, 19).replace(" ", "T")
        val inputDateTime = LocalDateTime.parse(dateTimeString)
        
        // Extract the time zone offset
        val offsetString = date.substring(date.lastIndexOf("(GMT") + 4, date.lastIndexOf(")"))
        val offsetSign = if (offsetString.startsWith("+")) 1 else -1
        val offsetHours = offsetString.substring(1).toInt() * offsetSign
        val inputTimeZone = TimeZone.of("Etc/GMT${if (offsetSign == 1) "-" else "+"}${offsetHours}")
    
        // Convert LocalDateTime to Instant using the input time zone
        val instant = inputDateTime.toInstant(inputTimeZone)
        
        // Convert Instant to the system default time zone
        val outputTimeZone = TimeZone.currentSystemDefault()
        val outputDateTime = instant.toLocalDateTime(outputTimeZone)
    
        return outputDateTime.toString().replace("T", " ")
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
    
    fun Int.formatString(): String {
        val numberString = this.toString()
        val length = numberString.length
        val sb = StringBuilder()
    
        for (i in 0 until length) {
            if ((length - i) % 3 == 0 && i != 0) {
                sb.append(',')
            }
            sb.append(numberString[i])
        }
    
        return sb.toString()
    }

}