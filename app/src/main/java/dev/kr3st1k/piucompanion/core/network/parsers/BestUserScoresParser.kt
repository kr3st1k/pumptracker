package dev.kr3st1k.piucompanion.core.network.parsers

import dev.kr3st1k.piucompanion.core.helpers.Utils
import dev.kr3st1k.piucompanion.core.helpers.Utils.getBackgroundImg
import dev.kr3st1k.piucompanion.core.network.data.BestUserScore
import dev.kr3st1k.piucompanion.core.network.data.BgInfo
import dev.kr3st1k.piucompanion.core.network.data.LoadableList
import dev.kr3st1k.piucompanion.core.prefs.BgManager
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.Locale

object BestUserScoresParser : Parser<LoadableList<BestUserScore>>() {

    private fun parseBestScore(element: Element, bgs: MutableList<BgInfo>): BestUserScore? {
        val songName = element.select("div.song_name").select("p").first()?.text() ?: return null
        var bg = bgs.find { tt -> tt.song_name == songName }?.jacket
        if (bg == null) bg = "https://www.piugame.com/l_img/bg1.png"

        val diffElements = element.select("div.numw.flex.vc.hc")
        val typeDiffImgUri = getBackgroundImg(
            element.select("div.stepBall_in.flex.vc.col.hc.wrap.bgfix.cont").first()!!, false
        )
        val typeDiff = Utils.parseTypeDifficultyFromUriBestScore(typeDiffImgUri)!!

        val diff = diffElements.select("img").map { Utils.parseDifficultyFromUri(it.attr("src")) }
            .joinToString("").let { typeDiff.uppercase(Locale.ENGLISH) + it }

        val scoreInfoElement = element.select("ul.list.flex.vc.hc.wrap")
        val score = scoreInfoElement.select("span.num").text()
        val rankImg = scoreInfoElement.select("img").first()!!.attr("src")
        val rank = Utils.parseRankFromUri(rankImg).toString()
            .uppercase(Locale.ENGLISH).replace("_p", "+").replace("_P", "+")

        return BestUserScore(songName, bg, diff, score, rank)
    }

    override fun parse(document: Document): LoadableList<BestUserScore> {
        val resList = mutableListOf<BestUserScore>()
        var isLoadMore = false
        val bgs = BgManager().readBgJson()

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
                val bestUserScore = parseBestScore(element, bgs) ?: BestUserScore()
                resList.add(bestUserScore)
            }
            isLoadMore = document.select("button.icon").isEmpty()

        }
        return LoadableList(resList, isLoadMore)
    }
}