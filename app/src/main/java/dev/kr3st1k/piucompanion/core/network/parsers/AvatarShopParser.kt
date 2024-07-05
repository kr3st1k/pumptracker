package dev.kr3st1k.piucompanion.core.network.parsers

import dev.kr3st1k.piucompanion.core.helpers.Utils.getBackgroundImg
import dev.kr3st1k.piucompanion.core.network.data.avatar.AvatarItem
import dev.kr3st1k.piucompanion.core.network.data.avatar.AvatarShop
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

object AvatarShopParser : Parser<AvatarShop>() {

    private fun parseAvatarItem(element: Element): AvatarItem {

        val name = element.select("div.name_t").first()!!.text()

        val isBought = element.attr("class").toString() == "have"

        val isSelected =
            element.select("button.stateBox.bg2").isNotEmpty()

        val price = if (!isBought) element.select("i.tt.en").first()!!.text() else "0"

        val bg = getBackgroundImg(element.select("div.re.img.bgfix").first()!!, false)

        val value =
            element.select("div.state_w.mgL").first()?.select("form")?.first()?.select("input")
                ?.attr("value")?.toString() ?: "null"

        return AvatarItem(name, isBought, isSelected, price, bg, value)
    }

    override fun parse(document: Document): AvatarShop {
        val avatarTable = document.select("ul.avatar_shopList2.flex.wrap")
        val boughtAvatars = avatarTable.select("li.have")
        val nonBoughtAvatars = avatarTable.select("li.buy")

        val avatars: MutableList<AvatarItem> = mutableListOf()

        for (element in boughtAvatars)
            avatars.add(parseAvatarItem(element))

        for (element in nonBoughtAvatars)
            avatars.add(parseAvatarItem(element))

        return AvatarShop(UserParser.parse(document), avatars)
    }
}