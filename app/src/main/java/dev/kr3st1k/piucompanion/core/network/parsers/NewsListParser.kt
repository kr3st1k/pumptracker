package dev.kr3st1k.piucompanion.core.network.parsers

import dev.kr3st1k.piucompanion.core.helpers.Utils
import dev.kr3st1k.piucompanion.core.network.data.News
import org.jsoup.nodes.Document

object NewsListParser : Parser<MutableList<News>>() {
    override fun parse(document: Document): MutableList<News> {
        val table = document.select("tbody")

        val res = mutableListOf<News>()

        for (elem in table.select("tr")) {
            if (res.count() == 7) break
            val titleAndLinkElem = elem.select("td.w_tit").select("a")
            val title = titleAndLinkElem.text()
            val typeElem = elem.select("td.w_type").select("i")
            val type = typeElem.text()
            val link = titleAndLinkElem.attr("href")
            val id = Utils.getWrId(titleAndLinkElem.attr("href"))!!.toInt()

            if (type == "Notice" || type == "Event")
                res.add(News(title, id, type, link))
        }
        return res
    }

}