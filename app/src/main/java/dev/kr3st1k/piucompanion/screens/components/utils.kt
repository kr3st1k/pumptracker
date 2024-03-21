package dev.kr3st1k.piucompanion.screens.components

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

}