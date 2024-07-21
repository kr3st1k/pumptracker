package dev.kr3st1k.piucompanion.core.network.parsers

import com.fleeksoft.ksoup.nodes.Document
import dev.kr3st1k.piucompanion.core.helpers.Utils
import dev.kr3st1k.piucompanion.core.helpers.Utils.getBackgroundImg
import dev.kr3st1k.piucompanion.core.network.data.news.NewsBanner

object NewsBannerParser : Parser<MutableList<NewsBanner>>() {
    override fun parse(document: Document): MutableList<NewsBanner> {
        val uniqueElements = document.select("a.img.resize.bgfix")
            .map { it to it.attr("href") }
            .distinctBy { it.second }
            .map { it.first }

        return uniqueElements.map { element ->
            NewsBanner(
                Utils.getWrId(element.attr("href"))?.toInt() ?: 0,
                getBackgroundImg(element),
                element.attr("href")
            )
        }.toMutableList()
    }

}