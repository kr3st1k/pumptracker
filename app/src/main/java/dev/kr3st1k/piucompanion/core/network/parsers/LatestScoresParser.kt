package dev.kr3st1k.piucompanion.core.network.parsers

import dev.kr3st1k.piucompanion.core.helpers.Utils
import dev.kr3st1k.piucompanion.core.helpers.Utils.getBackgroundImg
import dev.kr3st1k.piucompanion.core.helpers.Utils.parseTypeDifficultyFromUri
import dev.kr3st1k.piucompanion.core.network.data.LatestScore
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.Locale

object LatestScoresParser : Parser<MutableList<LatestScore>>() {
    private fun parseLatestScore(element: Element): LatestScore {
        val songName = element.select("div.song_name.flex").select("p").text()

        val typeDiffImgUri = element.select("div.tw").select("img").attr("src")

        val typeDiff = parseTypeDifficultyFromUri(typeDiffImgUri)!!

        val bg = getBackgroundImg(element.select("div.in.bgfix").first()!!, false)

        val diffElems = element.select("div.imG")

        var diff = ""

        for (i in diffElems) {
            diff += Utils.parseDifficultyFromUri(i.select("img").attr("src"))
        }

        diff = typeDiff.uppercase(Locale.ENGLISH) + diff

        val scoreRankElement = element.select("div.li_in.ac")

        val score = scoreRankElement.select("i.tx").text()

        var rank = "F"

        if ("STAGE BREAK" !in score) {
            val rankImg = scoreRankElement.first()!!.select("img").attr("src")
            rank = Utils.parseRankFromUri(rankImg).toString()
            rank = rank.uppercase(Locale.ENGLISH).replace("_p", "+").replace("_P", "+")
                .replace("X_", "Broken ")
        }

        val datePlay = element.select("p.recently_date_tt").text()
        return LatestScore(
            songName,
            bg,
            diff,
            score,
            rank,
            datePlay
        )
    }

    override fun parse(document: Document): MutableList<LatestScore> {
        val res: MutableList<LatestScore> = mutableListOf()

        val scoreTable = document.select("ul.recently_playeList.flex.wrap")
        val scores = scoreTable.select("li").filter { element ->
            element.select("div.wrap_in").count() == 1
        }
        for (element in scores) {
            res.add(parseLatestScore(element))
        }
        return res
    }

}