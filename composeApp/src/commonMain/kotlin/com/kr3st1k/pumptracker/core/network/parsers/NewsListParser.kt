package com.kr3st1k.pumptracker.core.network.parsers

import com.fleeksoft.ksoup.nodes.Document
import com.kr3st1k.pumptracker.core.helpers.Utils
import com.kr3st1k.pumptracker.core.network.data.news.News

object NewsListParser : Parser<MutableList<News>>() {
    override fun parse(document: Document): MutableList<News> {
        val table = document.select("tbody")

        val res = mutableListOf<News>()

        for (elem in table.select("tr")) {
            val titleAndLinkElem = elem.select("td.w_tit").select("a")
            val title = titleAndLinkElem.text()
            val typeElem = elem.select("td.w_type").select("i")
            val type = typeElem.text()
            val link = titleAndLinkElem.attr("href")
            val id = Utils.getWrId(titleAndLinkElem.attr("href"))!!.toInt()
            res.add(News(title, id, type, link))
        }
        return res
    }

}