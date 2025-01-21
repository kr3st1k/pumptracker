package com.kr3st1k.pumptracker.core.network.parsers

import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Element
import com.kr3st1k.pumptracker.core.helpers.Utils
import com.kr3st1k.pumptracker.core.helpers.Utils.getBackgroundImg
import com.kr3st1k.pumptracker.core.network.data.LoadableList
import com.kr3st1k.pumptracker.core.network.data.score.BestUserScore

//import java.util.Locale

object BestUserScoresParser : Parser<LoadableList<BestUserScore>>() {

    private fun parseBestScore(element: Element): BestUserScore? {
        val songName = element.select("div.song_name").select("p").first()?.text() ?: return null

        val diffElements = element.select("div.numw.flex.vc.hc")
        val typeDiffImgUri = getBackgroundImg(
            element.select("div.stepBall_in.flex.vc.col.hc.wrap.bgfix.cont").first()!!, false
        )
        var typeDiff = Utils.parseTypeDifficultyFromUriBestScore(typeDiffImgUri)!!
        typeDiff = when (typeDiff) {
            "c" -> "CO-OP x"
            "u" -> "UCS"
            else -> typeDiff.uppercase()
        }

        val diff = diffElements.select("img").map { Utils.parseDifficultyFromUri(it.attr("src")) }
            .joinToString("").let { typeDiff + it.replace("null", "") }

        val scoreInfoElement = element.select("ul.list.flex.vc.hc.wrap")
        val score = scoreInfoElement.select("span.num").text()
        val rankImg = scoreInfoElement.select("img").first()!!.attr("src")
        val rank = Utils.parseRankFromUri(rankImg).toString().uppercase()
            .replace("_p", "+").replace("_P", "+")

        return BestUserScore(songName, diff, score, rank)
    }

    override fun parse(document: Document): LoadableList<BestUserScore> {
        val resList = mutableListOf<BestUserScore>()
        var isLoadMore = false
        var lastPage = 1
        var scoreCount = 0
        if (document.select("div.no_con").isNotEmpty()) {
            resList.add(
                BestUserScore()
            )
        } else {
            val scoreTable = document.select("ul.my_best_scoreList.flex.wrap")
            val scores = scoreTable.select("li").filter { element ->
                element.select("div.in").count() == 1
            }

            for (element in scores) {
                val bestUserScore = parseBestScore(element) ?: BestUserScore()
                resList.add(bestUserScore)
            }
            isLoadMore = document.select("i.xi.last").isNotEmpty()
            scoreCount = document.selectFirst("div.left.total_wrap > i.tt.t2")!!.text().toInt()

            if (isLoadMore) {
                val attr =
                    document.select("i.xi.last").first()!!.parent()!!.attr("onclick")
                lastPage = attr[attr.length - 2].digitToInt()
            }

        }
        return LoadableList(resList, isLoadMore, lastPage, scoreCount)
    }
}