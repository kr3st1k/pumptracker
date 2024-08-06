package com.kr3st1k.pumptracker.core.network.parsers

import com.fleeksoft.ksoup.nodes.Document
import com.kr3st1k.pumptracker.core.helpers.Utils.getBackgroundImg
import com.kr3st1k.pumptracker.core.network.data.User

object UserParser : Parser<User?>() {
    override fun parse(document: Document): User? {
        val profileBox = document.select("div.box0.inner.flex.vc.bgfix")
            .first()
        if (profileBox == null)
            return null
        val avatarBox = document.select("div.re.bgfix")
            .first()

        val backgroundUrl = getBackgroundImg(profileBox)
        val avatarUrl = getBackgroundImg(avatarBox!!, false)

        val profileTitleAndNameBox = document.select("div.name_w")
            .select("p")

        val title = profileTitleAndNameBox
            .first()!!
            .text()
        val name = profileTitleAndNameBox
            .last()!!
            .text()

        val recentGameLocation = document.select("div.time_w")
            .select("li")
            .last()!!
            .text()
            .replace("Recently Access Games : ", "")
        val coinValue = document.select("i.tt.en")
            .first()!!
            .text()

        return User(
            name, title, backgroundUrl, avatarUrl,
            recentGameLocation, coinValue, null, true
        )
    }

}