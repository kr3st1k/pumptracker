package dev.kr3st1k.piucompanion.core.network.parsers

import dev.kr3st1k.piucompanion.core.helpers.Utils
import dev.kr3st1k.piucompanion.core.helpers.Utils.getBackgroundImg
import dev.kr3st1k.piucompanion.core.helpers.Utils.parseTypeDifficultyFromUri
import dev.kr3st1k.piucompanion.core.network.data.Pumbility
import dev.kr3st1k.piucompanion.core.network.data.PumbilityScore
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.Locale

object PumbilityParser : Parser<Pumbility>() {

    private fun parsePumbilityScore(element: Element): PumbilityScore {

        val score = element
            .select("div.score")
            .first()!!
            .select("i.tt.en")
            .first()!!
            .text()

        val songName = element.select("div.profile_name").first()!!.select("p.t1").first()!!.text()

        val typeDiffImgUri = element.select("div.tw").select("img").attr("src")

        val typeDiff = parseTypeDifficultyFromUri(typeDiffImgUri)!!

        val bg = getBackgroundImg(element.select("div.re.bgfix").first()!!, false)

        val diffElems = element.select("div.imG")

        var diff = ""

        for (i in diffElems) {
            diff += Utils.parseDifficultyFromUri(i.select("img").attr("src"))
        }

        diff = typeDiff.uppercase(Locale.ENGLISH) + diff


        val rankImg = element.select("div.grade_wrap").first()!!.select("img").attr("src")
        var rank = Utils.parseRankFromUri(rankImg).toString()
        rank = rank.uppercase(Locale.ENGLISH).replace("_p", "+").replace("_P", "+")
            .replace("X_", "Broken ")

        val datePlay = element.select("div.date").first()!!.select("i.tt").text()


        return PumbilityScore(
            songName,
            bg,
            diff,
            score,
            rank,
            Utils.convertDateFromSite(datePlay)
        )
    }


    override fun parse(document: Document): Pumbility {
        val res: MutableList<PumbilityScore> = mutableListOf()

        val user = UserParser.parse(document)

        val scoreTable = document
            .select("div.rating_rangking_list_w.top_songSt.pumblitiySt.mgT1")
            .first()!!
            .select("ul.list")
            .first()!!
        val scores = scoreTable.select("li").filter { element ->
            element.select("div.in.flex.vc.wrap").count() == 1
        }
        for (element in scores) {
            res.add(parsePumbilityScore(element))
        }
        return Pumbility(user, res)
    }

}