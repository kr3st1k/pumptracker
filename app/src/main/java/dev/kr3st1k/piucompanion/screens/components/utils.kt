package dev.kr3st1k.piucompanion.screens.components

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
    private const val RANK = "https://www\\.piugame\\.com\\/l_img\\/grade\\/(\\w+)\\.png"
    fun parseDifficultyFromUri(uri: String): String? = getFirstRegex(DIFFICULTY, uri)

    fun parseRankFromUri(uri: String): String? = getFirstRegex(RANK, uri)

    fun parseTypeDifficultyFromUri(uri: String): String? = getFirstRegex(DIFFICULTYTYPE, uri)


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